var theUser;
function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  theUser = googleUser
  var profile = googleUser.getBasicProfile();
  console.log("ID: " + profile.getId()); // Don't send this directly to your server!
  console.log('Full Name: ' + profile.getName());
  console.log('Given Name: ' + profile.getGivenName());
  console.log('Family Name: ' + profile.getFamilyName());
  console.log("Image URL: " + profile.getImageUrl());
  console.log("Email: " + profile.getEmail());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  console.log("ID Token: " + id_token);
  signUp(theUser);
}

function signUp(theUser) {
  var profile = theUser.getBasicProfile();
  var fullname = profile.getName();
  var pass  =profile.getName();
  var email=  profile.getEmail();
  var input= {
    fullName : fullname,
    password : pass,
    emailId : email
  }

  $.ajax({
    url : apiBasePath + "/signup",
    type : "PUT",
    data : JSON.stringify(input),
    dataType : "json",
    contentType : "application/json",
    processData : false,
    success: function (response) {
       loginUser(response.accessToken,"/");
    },

    error: function (response) {
      alert(response.errorMessage);
    }
  });
}

function loginUser(accessToken) {
  window.localStorage.setItem('access_token', accessToken);
  document.cookie = 'QRAuthCookie=' + accessToken + "; path=/";
  (function() {
    'use strict';
    var snackbarContainer = document.querySelector('.mdl-js-snackbar');
    var showToastButton = document.querySelector('#demo-show-toast');

    var data = {
      message : "You have successfully logged In"
    };
    snackbarContainer.MaterialSnackbar.showSnackbar(data);
  }());
}

function signOut_common() {
  window.localStorage.removeItem('access_token');
  eraseCookieFromAllPaths('QRAuthCookie');
  (function() {
    'use strict';
    var snackbarContainer = document.querySelector('.mdl-js-snackbar');
    var showToastButton = document.querySelector('#demo-show-toast');

    var data = {
      message : "You have successfully logged out"
    };
    snackbarContainer.MaterialSnackbar.showSnackbar(data);
  }());
  setTimeout(function () {
    window.location.href = '/'
  }, 500)
}
function eraseCookieFromAllPaths(name) {
  // This function will attempt to remove a cookie from all paths.
  var pathBits = location.pathname.split('/');
  var pathCurrent = ' path=';

  // do a simple pathless delete first.
  document.cookie = name + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT;';

  for (var i = 0; i < pathBits.length; i++) {
    pathCurrent += ((pathCurrent.substr(-1) != '/') ? '/' : '')
        + pathBits[i];
    document.cookie = name + '=; expires=Thu, 01-Jan-1970 00:00:01 GMT;'
        + pathCurrent + ';';
  }
}



$("#loginForm").validate({
  rules: {
    password: "required",
    username: {
      required: true,
      email: true
    }
  },
  messages: {
    password: "min password",
    email: {
      required: "We need your email address to contact you",
      email: "Your email address must be in the format of name@domain.com"
    }
  },
  submitHandler: function (form) {
    loginQRUser();
  }

});


function loginQRUser() {
  var input = {
    emailId : $('#loginUsername').val(),
    password : $('#loginPassword').val()
  };
  if($('#MdlCheckBox:checkbox:checked').length > 0 == true){
    localStorage.setItem("userName",$('#loginUsername').val());
    localStorage.setItem("password",$('#loginPassword').val());
  }
  $.ajax({
    url: apiBasePath + "/login",
    type: "PUT",
    data: JSON.stringify(input),
    dataType: "json",
    contentType: "application/json",
    processData: false,
    success: function (response) {
      if (response.loginSucces == true){
        loginUserSignedUoUser(response.accessToken, "/");
        $("#signout").show();
      }else {
        $('#loginError').html(response.errorMessage);
      }
    },

    error: function (response) {
      $('#loginError').html(response.errorMessage);
      $("#signout").hide();
    }
  });
}



$(document).ready(function () {
  // $('#loginForm').hide();
  $('#forgotpassword').hide();
  $("#signout").hide();

  $('#login').on('click',function () {
    $('#register').show();
    $('#login').hide();
    $('#loginForm').show();
    $('#forgotpassword').show();
    $('#signupform').hide();
  })
});
$('#register').on('click',function () {
  $('#register').hide();
  // $('#loginForm').hide();
  $('#forgotpassword').hide();
  $('#signupform').show();
  $('#login').show();

});




$("#signupForm").validate({
  rules: {
    password: "required",
    email: {
      required: true,
      email: true
    },
    username:"required"
  },
  messages: {
    password: "min password",
    username:"enter Your name .",
    email: {
      required: "We need your email address to contact you",
      email: "Your email address must be in the format of name@domain.com"
    }
  },
  submitHandler: function (form) {
    signUpForm();
  }

});


function signUpForm() {

  var input = {
    userName: $('#username').val(),
    password: $('#password').val(),
    emailId: $('#email').val()
  }

  alert(JSON.stringify(input));
  $.ajax({
    url: apiBasePath + "/signup",
    type: "PUT",
    data: JSON.stringify(input),
    dataType: "json",
    contentType: "application/json",
    processData: false,
    success: function (response) {
      loginUserSignedUoUser(response.accessToken, "/");

    },
    error: function (response) {
      alert(response.errorMessage);
    }
  });
}

function loginUserSignedUoUser(accessToken, redirectPage) {
  window.localStorage.setItem('access_token', accessToken);
  document.cookie = 'QRAuthCookie=' + accessToken + "; path=/";
  (function() {
    'use strict';
    var snackbarContainer = document.querySelector('.mdl-js-snackbar');
    var showToastButton = document.querySelector('#demo-show-toast');

    var data = {
      message : "You have successfully logged In"
    };
    snackbarContainer.MaterialSnackbar.showSnackbar(data);
  }());
  $("#signout").show();
  setTimeout(function () {
    window.location.href='/feeds'
  },500)
}


function createCommunity() {
  var  input = {
    communityName : "communityName",
    icon_url : "iconUrl",
    createdAt :"createdAt",
    descrrption : "descrrption",
    rules: "rules",
    slug : "slug"
  }

  $.ajax({
    url: apiBasePath + "/create-community",
    type: "PUT",
    data: JSON.stringify(input),
    dataType: "json",
    contentType: "application/json",
    processData: false,
    success: function (response) {
     alert("sucesss");

    },

    error: function (response) {
      alert(response.errorMessage);
    }
  });



}
