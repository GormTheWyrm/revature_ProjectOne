//loginscript and login forms need reconfigure...
//...need to separate employee from customer in api... for this script
//...maybe I can get the text from a span somewhere...
//this might need to be customerScript...

let password;
let username;
let isLoggedIn = false;
let accounts =[];
let customerName;
//do I need name here?

let baseUrl = "http://localhost:9000"; //update this
// console.log("test");
// userType = Employee or customer


//login function will use fetch to check pw and user against api
//if correct, pw and user should be saved
//pw and user can then be sent in header for next query...


//element variables and event handlers
let loginDiv = document.getElementById("loginDiv");

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

let userInput = document.getElementById("userInput"); //username element
let pwInput = document.getElementById("pwInput");   //pw element

let warning = document.getElementById("warning");

let actionSelect = document.getElementById("actionSelect");
let accountSelect = document.getElementById("accountSelect");
let selectFrom = document.getElementById("selectFrom");
let selectTo = document.getElementById("selectTo");


let userSpan = document.getElementById("userSpan");
// value variables
let userType = userSpan.textContent;
//this gives us the employee or customer we need to call the api...

function logout() {
    password = null;
    username = null;
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


function login(event) {
    //called by hitting login button
    event.preventDefault();
    console.log("this will fetch log in");
    if (validateUsername()) {
        if (validatePassword()) {
            // if (userType=="Employee"){
                // let url = baseUrl+ "/api/employee/"; //login is sngular
                // url += userInput.value;
                // console.log(url);
                // attemptLogin(url);
            // }
            // else if (userType=="Customer"){
                let url = baseUrl+ "/api/customer/";
                url += userInput.value;
                console.log(url);
                attemptLogin(url);
            // }
            //NEED TO VALIDATE THESE URLS...!!!
            //changing this to be customer specific!
        }
        else {
            //tells user password is not in acceptable format
            console.log("bad password");
        }
    }
    else {
        //tell user username is not in acceptable format
        console.log("username invalid")
    }
    //if pass, attemptSignup
    //might call attemptlogin... or make it part of signup...
    //if pass
}
function signup(event) {
    //called by hitting signin button...
    event.preventDefault();
    console.log("this will fetch sign in");
    if (validateUsername()) {
        if (validatePassword()) {
            //attempt signup - calls success if successful
            // if (userType=="Employee"){
            //     let url = baseUrl+ "/api/employee/";//signup is plural
            //     url += userInput.value;
            //     attemptSignup(url);
            // }
            // else if (userType=="Customer"){
                let url = baseUrl+ "/api/customer/";
                url += userInput.value;
                attemptSignup(url);
            // }
            //NEED TO VALIDATE THESE URLS...!!!
            //change this to just have customers?
        }
        else {
            //tells user password is not in acceptable format
            console.log("bad password");
        }
    }
    else {
        //tell user username is not in acceptable format
        console.log("username invalid")
    }
    //if pass, attemptSignup
    //might call attemptlogin... or make it part of signup...
    //if pass

}


function showSignup(event) {
    event.preventDefault();
    loginSubmit.style.display = "none";
    signupSpan.style.display = "none";
    loginSpan.style.display = "";
    signupSubmit.style.display = "";

}
function showLogin(event) {
    loginSubmit.style.display = "";
    signupSpan.style.display = "";
    loginSpan.style.display = "none";
    signupSubmit.style.display = "none";

}
function loginSuccess() {

    loginDiv.style.display = "none";
    bodyDiv.style.display = "";
}

function hideLoginDiv(){
    loginDiv.style.display = "none";
    bodyDiv.style.display = "";
}

function attemptSignup(urlVar) {
    //fetch
    // fetch(url, )// replace with a post?
    console.log("sign up fetch not connected yet");
    fetch(urlVar, {
        method: "POST",
        body: JSON.stringify({
            username: userInput.value,
            password: pwInput.value
        })

    })
    .then(res=>{
        res.json();
    })
    .then(data=>{
        //set password, username...
        // show accounts on results...
        //...do I need to do something for actions?
    })
    .catch(err=> {
        console.log(err);
        //data.error //would display the errormsg 
        //display some sort of warning to user
        warning.innerHTML = "Failed to connect to server";
        warning.style.display = "";
        //need to figure out other possible errors
    });
}//fix me!!!!!!!!!!!!!
function attemptLogin(urlVar) {
      //fetch
    // fetch(url, )// replace with a post?
    fetch(urlVar, {
        method: "POST",
        body: JSON.stringify({
            username: userInput.value,
            password: pwInput.value
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
    .then(res=>res.json())
    .then(data =>{
        //set password, username...
        // console.log(data)
        // console.log(data.json());  //this is undefined and I dont know why!
        // for some reason body is empty on here, not json
        
        //if statement here!!!
        username = data.username;
        password = data.password;
        customerName = data.name;
        nameSpan.innerText = username;
        console.log(data);
        // now save accounts... or at least appendthem...
        hideLoginDiv(); //hides login and shows body
        let htmlStr = "";
        for (i=0; i<data.accounts.length; i++){
            let account = data.accounts[i];
            // console.log(account);
            //accountNumber, approvedByEmployeeId
            //balance, status, username
            htmlStr += `<tr><td>${account.accountNumber}</td><td>${account.balance}</td><td>${account.status}</td></tr>`;
            //also need to update selectActions...

            let ele = document.createElement("OPTION");
            ele.value = data.accounts[i].accountNumber;
            ele.innerText = "Account Number: "+data.accounts[i].accountNumber;
            selectFrom.appendChild(ele);
            ele = document.createElement("OPTION");
            ele.value = data.accounts[i].accountNumber;
            ele.innerText = "Account Number: "+data.accounts[i].accountNumber;
            selectTo.appendChild(ele);
        }
        // let accountTable = document.getElementById("accountTable");

        let tableBody = document.getElementById("tableBody");
        tableBody.innerHTML =htmlStr; //will not work on IE...
        // tableBody is problematic... for IE
        //WORKING HERE!!~~~~~~~~~~~~
        // show accounts on results...
        // ...do I need to do something for actions?
    })
    .catch(err=> {
        console.log(err);
        //display some sort of warning to user
        warning.innerHTML = "Failed to connect to server";
        warning.style.display = "";
        //need to figure out other possible errors
    });
}

function validateUsername() { //validates username and password
    //return true-false
    //if
    console.log(userInput.value);
    // if (userInput.value.test()){
    if (userInput.value != "") {
        //temp validation

        console.log("username passes validation");
        return true;
        //search() returns index or -1; if index != 0...
        //that may have issues...
    }
    else {
        console.log("password fails validation");
        return false;
    }
    //fix this!!!
}
function validatePassword() {
    if (pwInput.value != "") {
        //temp validation

        console.log("password passes validation");
        return true;
        //search() returns index or -1; if index != 0...
        //that may have issues...
    }
    else {
        console.log("password fails validation");
        return false;
    }
} //fixme !!! needs better validation no /\... at least




//CUSTOMER ACTIONS
//withdraw, deposit transfer
//apply for account
//view transactions for an account... 


// let nameSpan = document.getElementById("nameSpan");
// let tableBody = document.getElementById("tableBody");


//onSelect functions:
actionSelect.addEventListener("change", function(){
    if(actionSelect.value == "withdraw"){
        showFrom();
    }
    else if(actionSelect.value == "deposit"){
        showTo();
    }
    else if(actionSelect.value == "transfer"){
        showFromTo();
    }
})
function showFrom(){
console.log("test")
selectFrom.style.display = "";
selectTo.style.display = "none";
}
function showTo(){
    selectFrom.style.display = "none";
    selectTo.style.display = "";
}
function showFromTo(){
    selectFrom.style.display = "";
    selectTo.style.display = "";
}

// need to set up options when get data...
//get options info from accounts array!
// ...updates to array...
//update accounts function when getting new data
//update select actions function? as part of above!

let updateButton = document.getElementById("updateButton");
updateButton.addEventListener("click", function(event){
    event.preventDefault();
    //this needs to updateaccounts and selectActions...
    console.log("accountSelect");
    //first set url based on what method being used...
    //ifs...
    console.log(actionSelect);
    if(actionSelect.value == "withdraw"){
        console.log("this will withdraw");
    }
    else if (actionSelect.value == "deposit"){
        console.log("this will deposit");
    }
    else if (actionSelect.value == "transfer"){
        console.log("this will transfer");
    }
    // add new actions /features here!
    else{

    }
    //need an applyforaccount option - that should disable amount
    // - no; it should have an amount!
    // - means rewriting the apply for new customer method...in dao

    //withdrawal, deposit, transfer...

    //then fetch...




///WIP function!
/*
    fetch(urlVar, {
        method: "POST",
        body: JSON.stringify({
            username: userInput.value,
            password: pwInput.value
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
    .then(res=>res.json())
    .then(data =>{
        //set password, username...
        // console.log(data)
        // console.log(data.json());  //this is undefined and I dont know why!
        // for some reason body is empty on here, not json
        
        //if statement here!!!
        username = data.username;
        password = data.password;
        customerName = data.name;
        nameSpan.innerText = username;
        console.log(data);
        // now save accounts... or at least appendthem...
        hideLoginDiv(); //hides login and shows body
        let htmlStr = "";
        for (i=0; i<data.accounts.length; i++){
            let account = data.accounts[i];
            // console.log(account);
            //accountNumber, approvedByEmployeeId
            //balance, status, username
            htmlStr += `<tr><td>${account.accountNumber}</td><td>${account.balance}</td><td>${account.status}</td></tr>`;
            //also need to update selectActions...

            let ele = document.createElement("OPTION");
            ele.value = data.accounts[i].accountNumber;
            ele.innerText = "Account Number: "+data.accounts[i].accountNumber;
            selectFrom.appendChild(ele);
            ele = document.createElement("OPTION");
            ele.value = data.accounts[i].accountNumber;
            ele.innerText = "Account Number: "+data.accounts[i].accountNumber;
            selectTo.appendChild(ele);
        }
        // let accountTable = document.getElementById("accountTable");

        let tableBody = document.getElementById("tableBody");
        tableBody.innerHTML =htmlStr; //will not work on IE...
        // tableBody is problematic... for IE
        //WORKING HERE!!~~~~~~~~~~~~
        // show accounts on results...
        // ...do I need to do something for actions?
    })
    .catch(err=> {
        console.log(err);
        //display some sort of warning to user
        warning.innerHTML = "Failed to connect to server";
        warning.style.display = "";
        //need to figure out other possible errors
    });

*/











});

