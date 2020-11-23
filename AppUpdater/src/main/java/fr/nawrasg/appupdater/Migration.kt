package fr.nawrasg.appupdater

abstract class Migration (val startVersion: Int, val endVersion: Int){
    abstract fun migrate(): Boolean;
}