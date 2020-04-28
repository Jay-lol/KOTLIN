package practice.kotlin.com.mygallery

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import kotlin.concurrent.timer

private const val REQUEST_READ_EXTERNAL_STORAGE = 1000

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 권한 없을시
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.
                    permission.READ_EXTERNAL_STORAGE)){
                // shouldShowRequestPermissionRationable이 이전에 권한을 거부했는지 반환해줌,
                // 한번 거부 했을시
                alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다", "권한이 필요한 이유"){
                    yesButton {
                        // 권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {  }
                }.show()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
            }
        } else {
            getAllPhotos()
        }
    }


    private fun getAllPhotos(){
        //모든 사진 정보 가져오기, 프로바이더를 사용해 contentResolver 객체를 사용
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")
        val fragments = ArrayList<Fragment>()

        if(cursor != null){
            while(cursor.moveToNext()){
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d("Mainactivity", uri)
                fragments.add(PhotoFragment.newInstance(uri))
            }
            cursor.close()      // Cursor 객체는 더 이상 사용하지 않으면 close로 메모리 누수 방지!
        }

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter

        timer(period = 3000){
            runOnUiThread{
                if(viewPager.currentItem < adapter.count - 1){
                    viewPager.currentItem = viewPager.currentItem + 1
                } else {    // 맨끝이면 처음꺼
                    viewPager.currentItem = 0
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)){       // 권한을 한개만 요청했으므로 0번만 확인..
                getAllPhotos()
                } else {
                    toast("권한 거부 됨")
                }
                return          // PERMISSION 반환
            }
        }
    }


}
