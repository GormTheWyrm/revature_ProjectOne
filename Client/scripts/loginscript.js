//loginscript and login forms need reconfigure...


let password;
let username;
let baseUrl = "http://localhost:9000"; //update this
// console.log("test");



//login function will use fetch to check pw and user against api
//if correct, pw and user should be saved
//pw and user can then be sent in header for next query...

let loginDiv = document.getElementById("loginDiv");
// loginDiv.addEventListener("click", login);
let signupDiv = document.getElementById("signupDiv");
// signupDiv.addEventListener("click", signup);
//event handlers
let loginSubmit = document.getElementById("loginSubmit");
loginSubmit.addEventListener("click", login);

let signupSubmit = document.getElementById("signupSubmit");
signupSubmit.addEventListener("click", signup);

let logoutBtn = document.getElementById("logout");
logoutBtn.addEventListener("click", logout);

let signinSpan = document.getElementById("signinSpan");
signinSpan.addEventListener("click", showSignin);

let loginSpan = document.getElementById("loginSpan");
loginSpan.addEventListener("click", showLogin);

let credentialDiv = document.getElementById("credentialDiv");


function logout(){
password = null;
username =null;
window.location.reload();
//need to worry about cache keeping pw and username?
}


//multi-part form!

//choose to sign in or sign up...
//sign up form shows at first
//can click login to close signin form and open login form
//opposite should be true
// simply hide form!?
//login and signin buttons make different fetch calls...
//and employee/customer changes url...


function login(event){
    event.preventDefault();
    console.log("this will fetch login");

    //validation
    //if pass, attemptLogin
    //if pass
    //loginsuccess()
}
function signup(event){
    
    event.preventDefault();
    console.log("this will fetch sign in");
        //validation
    //if pass, attemptSignup
        //might call attemptlogin... or make it part of signup...
    //if pass
    //loginsuccess()
}


function showSignin(event){
    event.preventDefault();
    loginDiv.style.display = "none";
    signupDiv.style.display = "";
}
function showLogin(event){
    event.preventDefault(event);
    signupDiv.style.display = "none";
    loginDiv.style.display = "";
}
function loginSuccess(){
    
    credentialDiv.style.display = "none";
    // loginDiv.style.display = "none";
    

}


