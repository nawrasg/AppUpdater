package fr.nawrasg.appupdater

import android.content.Context
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import androidx.preference.PreferenceManager

class AppUpdater(
    private val context: Context,
    private val migrations: MutableList<Migration>?
) {

    companion object {
        const val TAG = "AppUpdater";
        const val CURRENT_VERSION_CODE = "fr.nawrasg.appupdater-current_version_code"
    }

    private constructor(builder: Builder) : this(builder.context, builder.migrations)
    private val resultList: MutableList<Boolean> = ArrayList()

    fun execute() {
        if (this.migrations?.size!! > 0) {
            this.migrations.sortBy { it.startVersion }
            if (getLastRegisteredVersionCode() == -1) {
                if (getCurrentVersionCode() == 1) {
                    updateLastRegisteredVersionCode()
                } else {
                    val filteredMigrations =
                        this.migrations.filter { it.endVersion <= getCurrentVersionCode() }
                    filteredMigrations.forEach { resultList.add(it.migrate()) }
                    if(resultList.all { it }){
                        updateLastRegisteredVersionCode()
                    }
                }
            } else if (getLastRegisteredVersionCode() < getCurrentVersionCode()) {
                val filteredMigrations =
                    this.migrations.filter { it.startVersion >= getLastRegisteredVersionCode() && it.endVersion <= getCurrentVersionCode() }
                filteredMigrations.forEach { resultList.add(it.migrate()) }
                if(resultList.all { it }){
                    updateLastRegisteredVersionCode()
                }
            }
        }
    }

    private fun getCurrentVersionCode(): Int {
        val info = this.context.packageManager.getPackageInfo(this.context.packageName, 0)
        return PackageInfoCompat.getLongVersionCode(info).toInt()
    }

    private fun getLastRegisteredVersionCode(): Int {
        val nPref = PreferenceManager.getDefaultSharedPreferences(this.context)
        return nPref.getInt(CURRENT_VERSION_CODE, -1)
    }

    private fun updateLastRegisteredVersionCode() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this.context)
        val editor = pref.edit()
        editor.putInt(CURRENT_VERSION_CODE, getCurrentVersionCode())
        editor.apply()
    }

    class Builder(val context: Context) {
        val migrations: MutableList<Migration> = ArrayList()

        //throw exception if endVersion > startVersion
        fun addMigration(migration: Migration) = apply { this.migrations.add(migration) }

        fun build() = AppUpdater(this)
    }

}