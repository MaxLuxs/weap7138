package luxs.max.weap7138.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateService(): Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("!!!Service:", "I am born")
        CoroutineScope(Dispatchers.IO).launch {
            downloadDataEveryTenSecond()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.i("!!!Service", " Service was dead=*( ")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private suspend fun downloadDataEveryTenSecond(){
        while (true){
            Thread.sleep(10000)
            Log.i("!!!Service:", "Прошло 10 секунд.")
        }
    }

}