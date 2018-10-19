package org.wit.quest.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.wit.quest.R
import org.wit.quest.main.MainApp
import org.wit.quest.models.UserModel

class LoginActivity : AppCompatActivity() {

  lateinit var app : MainApp
  var user = UserModel()

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    app = application as MainApp

    signin.setOnClickListener { login() }

    signup.setOnClickListener { singup() }
  }

  fun singup() {

    user.email = useremail.text.toString()
    user.password = userpassword.text.toString()

    if (user.email.isEmpty() || !user.email.contains("@") || user.password.isEmpty() || user.password.length < 4 || user.password.length > 10) {
      onSignUpFailed()
      return
    }

    if (app.users.signup(user)) {
      onSignUpFailed()
      return
    }

    app.users.create(user.copy())

    Toast.makeText(baseContext, "Sign Up Complete, Please Sign In" , Toast.LENGTH_LONG).show()
    useremail.setText("")
    userpassword.setText("")

  }

  fun login() {

    user.email = useremail.text.toString()
    user.password = userpassword.text.toString()

    if (user.email.isEmpty() || !user.email.contains("@") || user.password.isEmpty() || user.password.length < 4 || user.password.length > 10) {
      onLoginFailed()
      return
    }

    if (app.users.login(user)) {
      onLoginFailed()
      return
    }

    android.os.Handler().postDelayed(
        {
          // On complete call either onLoginSuccess or onLoginFailed
          onLoginSuccess()
          // onLoginFailed();
        }, 1000)
  }


  override fun onBackPressed() {
    // Disable going back to the MainActivity
    moveTaskToBack(true)
  }

  fun onLoginSuccess() {
    startActivity(intentFor<HomeActivity>())
    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    finish()
  }

  fun onLoginFailed() {
    Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()
  }


  fun onSignUpFailed() {
    Toast.makeText(baseContext, "Sign Up failed", Toast.LENGTH_LONG).show()
  }

}