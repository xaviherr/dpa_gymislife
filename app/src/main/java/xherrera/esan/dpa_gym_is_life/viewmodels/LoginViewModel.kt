package xherrera.esan.dpa_gym_is_life.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {



    val employee = hashMapOf(
        "employee_id" to "12345678",
        "employee_type_id" to "1234",
        "name" to "Sam Bigotes"
    )

}