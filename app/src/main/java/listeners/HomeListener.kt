package listeners

interface HomeListener {
    fun onHomeDataChangeListener(toolbarVisibility: Boolean, backBtnVisibility: Boolean,newTitle: String,)
}