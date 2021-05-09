//loginscript and login forms need reconfigure...
//...need to separate employee from customer in api... for this script
//...maybe I can get the text from a span somewhere...


let password;
let username;
let baseUrl = "http://localhost:9000"; //update this
// console.log("test");



//login function will use fetch to check pw and user against api
//if correct, pw and user should be saved
//pw and user can then be sent in header for next query...

let loginDiv = document.getElementById("loginDiv");

//event handlers
let loginSubmit = document.getElementById("loginSubmit");
loginSubmit.addEventListener("click", login);

let signupSubmit = document.getElementById("signupSubmit");
signupSubmit.addEventListener("click", signup);

let logoutBtn = document.getElementById("logout");
logoutBtn.addEventListener("click", logout);

let signupSpan = document.getElementById("signupSpan");
signupSpan.addEventListener("click", showSignup);

let loginSpan = document.getElementById("loginSpan");
loginSpan.addEventListener("click", showLogin);

let bodyDiv = document.getElementById("bodyDiv"); 
let userSpan = document.getElementById("userSpan");
let userType = userSpan.textContent;
//this gives us the employee or customer we need to call the api...
console.log(userType); 

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
    loginSuccess();
}


function showSignup(event){
    event.preventDefault();
    loginSubmit.style.display = "none";
    signupSpan.style.display = "none";
    loginSpan.style.display = "";
    signupSubmit.style.display = "";
    
}
function showLogin(event){
    loginSubmit.style.display = "";
    signupSpan.style.display = "";
    loginSpan.style.display = "none";
    signupSubmit.style.display = "none";
    
}
function loginSuccess(){
    
    loginDiv.style.display = "none";
    bodyDiv.style.display = "";
    

}


