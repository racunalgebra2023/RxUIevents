package hr.algebra.rxuievents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding3.widget.textChanges
import hr.algebra.rxuievents.databinding.ActivityMainBinding
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity( ) {

    private lateinit var binding    : ActivityMainBinding
    private          var disposable : Disposable? = null

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView( binding.root )

        binding.bStartObserving.setOnClickListener {
            disposable = binding.etNumbers
                            .textChanges( )
                            .map { it.toString( ) }
                            .map { it.split( "" ) }
                            .map { nums -> nums.map { if( it.isEmpty() ) 0 else it.toInt( ) }.sum( ) }
                            .debounce( 800, TimeUnit.MILLISECONDS )
                            .observeOn( AndroidSchedulers.mainThread( ) )
                            .doOnError{ Toast.makeText( this, "Error while summing", Toast.LENGTH_SHORT ).show( ) }
                            .retry( )
                            .subscribe {
                                binding.tvResult.text = "$it"
                            }
        }

        binding.bStopObserving.setOnClickListener {
            disposable?.dispose( )
        }



/*
                .subscribe ( object : Observer<CharSequence> {
                    override fun onSubscribe( d: Disposable ) { }

                    override fun onNext(t: CharSequence) {
                        Toast.makeText( this@MainActivity, "$t", Toast.LENGTH_SHORT ).show( )
                    }

                    override fun onError(e: Throwable) { }

                    override fun onComplete() { }

                } )
*/
/*
                .subscribe ( object : Consumer< CharSequence >{
                    override fun accept( t: CharSequence? ) {
                        Toast.makeText( this@MainActivity, "$t", Toast.LENGTH_SHORT ).show( )
                    }
                } )
*/
    }
}