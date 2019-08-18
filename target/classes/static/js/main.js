if (isUserLoggedIn() == true) {
  var userId = parseJwt(getAuthToken()).id;
  var username = parseJwt(getAuthToken()).emailId;
  var name_sub = parseJwt(getAuthToken()).sub;
  var user_name = parseJwt(getAuthToken()).name;
  var user_role = parseJwt(getAuthToken()).role;
  var city = parseJwt(getAuthToken()).city;
  var phonenumber = parseJwt(getAuthToken()).phonenumber;
  var uniqueUserId = parseJwt(getAuthToken()).uniqueUserId;
  console.log(parseJwt(getAuthToken()));
}
$(document).ready(function () {
  if (isUserLoggedIn() == true){
    $("#signout").show();
  }
});
function getAuthToken() {
  return window.localStorage.getItem('access_token');
}
function parseJwt(token) {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace('-', '+').replace('_', '/');
  return JSON.parse(window.atob(base64));
};
function isUserLoggedIn() {

  var token = getAuthToken();
  var cookie = getCookie('QRAuthCookie');
  var currentDate = new Date();
  var timestampcurrent = toTimestamp(new Date());

  if (token && cookie) {
    if (token != "null" && token != "undefined" && cookie != "undefined"
        && token != "" && token != null && cookie != ''
        && cookie != null && cookie != "null"
        && timestampcurrent < parseJwt(token).exp) {
      return true;
    }
  }

  localStorage.removeItem("access_token");
  eraseCookieFromAllPaths('QRAuthCookie');
  return false;
}
function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}
function toTimestamp(strDate) {
  var datum = Date.parse(strDate);
  return datum / 1000;
}
