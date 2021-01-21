package luxs.max.weap7138.Services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateWorker(appContext:Context, workerParams:WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

}