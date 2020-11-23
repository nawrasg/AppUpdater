package fr.nawrasg.appupdaterexample

import android.app.Application
import android.util.Log
import fr.nawrasg.appupdater.AppUpdater
import fr.nawrasg.appupdater.Migration

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val MIGRATION_1_2 = object: Migration(1, 2){
            override fun migrate(): Boolean {
                Log.d("App", "1 -> 2")
                return true
            }

        }

        val MIGRATION_2_3 = object: Migration(2, 3){
            override fun migrate(): Boolean {
                Log.d("App", "2_3")
                return true
            }

        }

        val MIGRATION_3_4 = object: Migration(3, 4){
            override fun migrate(): Boolean {
                Log.d("App", "3_4")
                return true
            }

        }

        val MIGRATION_4_5 = object: Migration(4, 5){
            override fun migrate(): Boolean {
                Log.d("App", "4_5")
                return true
            }

        }

        val updater = AppUpdater.Builder(applicationContext)
            .addMigration(MIGRATION_1_2)
            .addMigration(MIGRATION_3_4)
            .addMigration(MIGRATION_2_3)
            .addMigration(MIGRATION_4_5)
            .build()

        updater.execute()
    }
}