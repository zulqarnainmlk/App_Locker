package listeners

interface AdapterListener {
    fun adapterData(key:String,position:Int)
    fun adapterVault(key:String,position: Int)
}