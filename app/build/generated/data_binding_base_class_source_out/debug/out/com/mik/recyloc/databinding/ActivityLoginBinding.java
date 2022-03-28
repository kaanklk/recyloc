// Generated by view binder compiler. Do not edit!
package com.mik.recyloc.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.mik.recyloc.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final CheckBox checkboxKeepMeLoggedIn;

  @NonNull
  public final EditText email;

  @NonNull
  public final Button login;

  @NonNull
  public final EditText password;

  @NonNull
  public final TextView textViewLogin;

  @NonNull
  public final TextView textViewReset;

  private ActivityLoginBinding(@NonNull LinearLayout rootView,
      @NonNull CheckBox checkboxKeepMeLoggedIn, @NonNull EditText email, @NonNull Button login,
      @NonNull EditText password, @NonNull TextView textViewLogin,
      @NonNull TextView textViewReset) {
    this.rootView = rootView;
    this.checkboxKeepMeLoggedIn = checkboxKeepMeLoggedIn;
    this.email = email;
    this.login = login;
    this.password = password;
    this.textViewLogin = textViewLogin;
    this.textViewReset = textViewReset;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.checkboxKeepMeLoggedIn;
      CheckBox checkboxKeepMeLoggedIn = ViewBindings.findChildViewById(rootView, id);
      if (checkboxKeepMeLoggedIn == null) {
        break missingId;
      }

      id = R.id.email;
      EditText email = ViewBindings.findChildViewById(rootView, id);
      if (email == null) {
        break missingId;
      }

      id = R.id.login;
      Button login = ViewBindings.findChildViewById(rootView, id);
      if (login == null) {
        break missingId;
      }

      id = R.id.password;
      EditText password = ViewBindings.findChildViewById(rootView, id);
      if (password == null) {
        break missingId;
      }

      id = R.id.textViewLogin;
      TextView textViewLogin = ViewBindings.findChildViewById(rootView, id);
      if (textViewLogin == null) {
        break missingId;
      }

      id = R.id.textViewReset;
      TextView textViewReset = ViewBindings.findChildViewById(rootView, id);
      if (textViewReset == null) {
        break missingId;
      }

      return new ActivityLoginBinding((LinearLayout) rootView, checkboxKeepMeLoggedIn, email, login,
          password, textViewLogin, textViewReset);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}