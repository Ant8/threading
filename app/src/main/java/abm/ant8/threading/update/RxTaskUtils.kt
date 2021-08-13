package abm.ant8.threading.update

import com.google.android.play.core.tasks.OnCompleteListener
import com.google.android.play.core.tasks.Task
import io.reactivex.Single
import io.reactivex.SingleEmitter

fun <T : Any> Task<T>.toSingle(): Single<T> = asSingle { addOnCompleteListener(SingleEmitterListener(it)) }

private fun <T : Any> Task<T>.asSingle(block: (SingleEmitter<T>) -> Unit): Single<T> =
    if (isComplete) Single.fromCallable(::getResult) else Single.create(block)

internal class SingleEmitterListener<T : Any>(private val emitter: SingleEmitter<T>) : OnCompleteListener<T> {
    override fun onComplete(task: Task<T>) = try {
        emitter.onSuccess(task.result)
    } catch (exception: Exception) {
        emitter.onError(exception)
    }
}
