package ayds.observer

interface Observable<T> {
    fun subscribe(observer: (Any) -> Unit)
    fun unSubscribe(observer: Observer<T>)
}

fun interface Observer<T> {
    fun update(value: T)
}

interface Publisher<T> {
    fun notify(value: T)
}