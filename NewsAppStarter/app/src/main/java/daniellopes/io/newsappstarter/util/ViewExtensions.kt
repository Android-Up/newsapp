package daniellopes.io.newsappstarter.util

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        requireContext(),
        message,
        duration
    ).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun String.getDateTimeFormatted(): String {
    //usamos um try catch aqui pois é podemos ter um ParseException na hora da conversão, então tratamos isso de forma que pegamos essa exception.
    try {
        // Crio um formato para a data que vem no servidor (Ela vem com ano, mes e dia)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", getLocale())
        // Formato a string para essa data fazendo um parse.
        // O this é a propria extensão da string que estamos fazendo, que no caso é a propriedade (publishedAt)
        val date = dateFormat.parse(this)
        //Temos uma data
        date?.let {
            // verifico se ela não é nula com o let, e encaminho para uma função que vai formatar essa data do jeito que preciso.
            //esse this.substring(11..18), é um range que fazemos pois a data vem com hora, então fazemos a concatenação da data mais a hora
            // caso fique na duvida, pesquise sobre a função substring das Strings.
            return getDateToStringFormatted(date, "dd/MM/yyyy") + " - " + this.substring(11..18)
        }
    } catch (e: ParseException) {
        e.localizedMessage?.let {
            Log.d("TAG", "getDateTimeFormatted: $e")
        }
    }
    return orEmpty()
}

fun getDateToStringFormatted(date: Date, dateString: String): String {
    val simpleDateFormat = SimpleDateFormat(dateString, getLocale())
    return simpleDateFormat.format(date)
}

fun getLocale(): Locale {
    return Locale("pt", "BR")
}