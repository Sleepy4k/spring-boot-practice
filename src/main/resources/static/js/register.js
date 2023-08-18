window.onload = function () {
  document.getElementById("register").addEventListener("click", login);
}

function login() {
  const name = document.getElementById("name").value;
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;
  const password_confirmation = document.getElementById("password_confirmation").value;

  if (name == "") {
    alert("Name cannot be empty!");
    return;
  } else if (username == "") {
    alert("Username cannot be empty!");
    return;
  } else if (password == "") {
    alert("Password cannot be empty!");
    return;
  } else if (password_confirmation == "") {
    alert("Password confirm cannot be empty!");
    return;
  }

  if (name.length > 100) {
    alert("Name must be less than 100 characters!");
    return;
  } else if (username.length < 6 || username.length > 100) {
    alert("Username must be between 6 and 100 characters!");
    return;
  } else if (password.length > 100) {
    alert("Password must be less than 100 characters!");
    return;
  } else if (password_confirmation.length > 100) {
    alert("Password confirm must be less than 100 characters!");
    return;
  }

  if (password != password_confirmation) {
    alert("Password and password confirm do not match!");
    return;
  }

  const xmlhttp = new XMLHttpRequest();
  xmlhttp.open("post", "/api/auth/register", true);
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify({name: name, username: username, password: password}));
  xmlhttp.onreadystatechange = function () {
    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
      alert("Register successful!");
      window.location.href = "/login";
    } else {
      console.log("Error: " + xmlhttp.status + " " + xmlhttp.statusText);
    }
  }
}