package xherrera.esan.dpa_gym_is_life.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xherrera.esan.dpa_gym_is_life.R
import xherrera.esan.dpa_gym_is_life.viewmodels.LoginViewModel


class LoginFragment : Fragment() {

    private val model: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        readFromFirestore()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun readFromFirestore(){
        val gymDb = Firebase.firestore
        gymDb.collection("gymapp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

}