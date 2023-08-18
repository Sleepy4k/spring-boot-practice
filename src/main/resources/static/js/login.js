window.onload = function () {
  document.getElementById("login").addEventListener("click", login);
}

function login() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  if (username == "") {
    alert("Username cannot be empty!");
    return;
  } else if (password == "") {
    alert("Password cannot be empty!");
    return;
  }

  if (username.length < 6 || username.length > 100) {
    alert("Username must be between 6 and 100 characters!");
    return;
  } else if (password.length > 100) {
    alert("Password must be less than 100 characters!");
    return;
  }

  const xmlhttp = new XMLHttpRequest();
  xmlhttp.open("post", "/api/auth/login", true);
  xmlhttp.setRequestHeader("Content-type", "application/json");
  xmlhttp.send(JSON.stringify({username: username, password: password}));
  xmlhttp.onreadystatechange = function () {
    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
      alert("Login successful!");
      window.location.href = "/";
    } else {
      console.log("Error: " + xmlhttp.status + " " + xmlhttp.statusText);
    }
  }
}