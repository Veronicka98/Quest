package org.wit.quest.views

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

    // set up listeners for buttons
    signin.setOnClickListener {login()}
    signup.setOnClickListener {singup()}
  }

  fun singup() {
    // get use input
    user.email = useremail.text.toString()
    user.password = userpassword.text.toString()

    // check if input is valid
    if (user.email.isEmpty() || !user.email.contains("@") || user.password.isEmpty() || user.password.length < 4 || user.password.length > 10) {
      onSignUpFailed()
      return
    }

    // check if user can be signed up
    if (app.users.signup(user)) {
      onSignUpFailed()
      return
    }

    // create new user
    app.users.create(user.copy())

    // inform user of sign up
    Toast.makeText(baseContext, "Sign Up Successful" , Toast.LENGTH_LONG).show()

    // reset text fields
    useremail.setText("")
    userpassword.setText("")

  }

  fun login() {
    // get user input
    user.email = useremail.text.toString()
    user.password = userpassword.text.toString()

    // check if input is valid
    if (user.email.isEmpty() || !user.email.contains("@") || user.password.isEmpty() || user.password.length < 4 || user.password.length > 10) {
      onLoginFailed()
      return
    }

    // check if login is sucessful
    if (app.users.login(user)) {
      onLoginFailed()
      return
    }

    android.os.Handler().postDelayed({onLoginSuccess()}, 1000)
  }

  override fun onBackPressed() {
    moveTaskToBack(true)
  }

  fun onLoginSuccess() {
    Toast.makeText(baseContext, "Sign In Successful", Toast.LENGTH_LONG).show()
    // start home activity if login sucessful
    startActivity(intentFor<HomeActivity>())
    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    // finish login activity
    finish()
  }

  fun onLoginFailed() {
    Toast.makeText(baseContext, "Sign In failed", Toast.LENGTH_LONG).show()
  }

  fun onSignUpFailed() {
    Toast.makeText(baseContext, "Sign Up failed", Toast.LENGTH_LONG).show()
  }

}