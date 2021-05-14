

let password;
let username;
let isLoggedIn = false;
let accounts = [];
let employeeName;
//do I need name here?

let baseUrl = "http://localhost:9000"; //update this


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

let numDiv = document.getElementById("numDiv");
let textDiv = document.getElementById("textDiv");

let customerInput = document.getElementById("customerInput");//amount to transfer, deposit or withdraw
let accountInput = document.getElementById("accountInput");
let resultsDiv = document.getElementById("resultsDiv");

let tableHead = document.getElementById("tableHead");

let nameInput = document.getElementById("nameInput");
let bodyWarning = document.getElementById("bodyWarning");
let nameRow = document.getElementById("nameRow");


let fetchButton = document.getElementById("fetchButton");
fetchButton.addEventListener("click", fetchResults);

let userSpan = document.getElementById("userSpan");
// value variables
let userType = userSpan.textContent;
//this gives us the employee or employee we need to call the api...
//depreciateed

function logout() {
    password = null;
    username = null;
    window.location.reload();
    //need to worry about cache keeping pw and username?
}

function login(event) {
    //called by hitting login button
    event.preventDefault();
    console.log("this will fetch log in");
    if (validateUsername()) {
        if (validatePassword()) {
            //set up employeelogin api url
            let url = baseUrl + "/api/employee/";
            url += userInput.value;
            console.log(url);
            attemptLogin(url);
        }
        else {
            //tells user password is not in acceptable format
            // console.log("bad password");
            //validate password function informs user of error
        }
    }
    else {
        //tell user username is not in acceptable format
        console.log("username invalid")
    }
}
function signup(event) {
    //called by hitting signup button...
    event.preventDefault();
    console.log("this will fetch sign in");
    if (validateUsername()) {
        if (validatePassword()) {
            //set api url for employee signup
            let url = baseUrl + "/api/employees";
            attemptSignup(url);
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
}


function showSignup(event) {
    event.preventDefault();
    loginSubmit.style.display = "none";
    signupSpan.style.display = "none";
    loginSpan.style.display = "";
    signupSubmit.style.display = "";
    nameRow.style.display = "";
}
function showLogin(event) {
    loginSubmit.style.display = "";
    signupSpan.style.display = "";
    loginSpan.style.display = "none";
    signupSubmit.style.display = "none";
    nameRow.style.display = "none";
}
function loginSuccess() {

    loginDiv.style.display = "none";
    bodyDiv.style.display = "";
}

function hideLoginDiv() {
    loginDiv.style.display = "none";
    bodyDiv.style.display = "";
}

function attemptSignup(urlVar) {
    //fetch
    console.log("sign up fetch ");
    fetch(urlVar, {
        method: "POST",
        body: JSON.stringify({
            username: userInput.value,
            password: pwInput.value,
            name: nameInput.value
        })
    })
        .then(res => res.json())    //copied from login
        .then(data => {
            if (data) {
                if (data.username) {
                    username = data.username;
                    password = data.password;
                    employeeName = data.name;
                    nameSpan.innerText = username;
                    hideLoginDiv(); //hides login and shows body
                    let htmlStr = "";
                }
                else if (data.error) {
                    warning.innerHTML = "Username or Password incorrect";
                    warning.style.display = "";
                }

            }
            else {
                //if no data but no error... inform user there was a problem
                warning.innerHTML = "Error processing request";
                warning.style.display = "";
            }
        })
        .catch(err => {
            console.log(err);
            //display some sort of warning to user
            warning.innerHTML = "Failed to connect to server";
            warning.style.display = "";
            //need to figure out other possible errors
        });
}
function attemptLogin(urlVar) { //LOGIN
    warning.style.display = "none"; //clears old warning
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
        .then(res => res.json())
        .then(data => {
            if (data) {
                if (data.username) {
                    username = data.username;
                    password = data.password;
                    employeeName = data.name;
                    nameSpan.innerText = username;
                    hideLoginDiv(); //hides login and shows body
                }
                else if (data.error) {
                    warning.innerHTML = "Username or Password incorrect";
                    warning.style.display = "";
                }
            }
            else {
                //if no data but no error... inform user there was a problem
                warning.innerHTML = "Error processing request";
                warning.style.display = "";
            }
        })
        .catch(err => {
            console.log(err);
            //display some sort of warning to user
            warning.innerHTML = "Failed to connect to server";
            warning.style.display = "";
            //need to figure out other possible errors
        });
}

function validateUsername() { //validates username and password
    //return true-false
    console.log(userInput.value);
    // if (userInput.value.test()){
    if (userInput.value != "") {
        //temp validation
        if (userInput.value.match(/[^A-Za-z1234567890!@#$%-]/)) {
            console.log("password fails validation");
            //deploy warning msg!
            warning.innerHTML = "username contains invalid symbols: valid symbols are letters, numbers and !@#$%-";
            warning.style.display = "";
            return false;
        }
        else {
            return true;

        }
    }
    else {
        console.log("password fails validation: cannot be empty");
        //deploy warning msg!
        warning.innerHTML = "username cannot be empty";
        warning.style.display = "";
        return false;
    }
    //fix this!!!
}
function validatePassword() {
    if (pwInput.value != "") {
        if (userInput.value.match(/[^A-Za-z1234567890!@#$%^&*()-+=]/)) {
            console.log("password fails validation");
            //deploy warning msg!
            warning.innerHTML = "password contains invalid symbols: valid symbols are letters, numbers and !@#$%^&*()-+=";
            warning.style.display = "";
            return false;
        }
        else {
            return true;

        }
    }
    else {
        console.log("password fails validation");

        warning.innerHTML = "password cannot be empty";
        warning.style.display = "";
        return false;
    }
}




//employee ACTIONS

//onSelect functions:
actionSelect.addEventListener("change", function () {
    if (actionSelect.value == "viewCustomer") {
        showText();
    }
    else if (actionSelect.value == "viewAccount") {
        showNum();
    }
    else if (actionSelect.value == "viewPending") {
        showNone();
    }
    else if (actionSelect.value == "viewAllT") {
        showNone();
    }
    else if (actionSelect.value == "viewTByDay") {
        showDay();
    }
    else if (actionSelect.value == "viewTByAcc") {
        showNum();
    }
    else if (actionSelect.value == "viewTByCustomer") {
        showText();
    }
    else if (actionSelect.value == "approve") {
        showNum();
    }
    else if (actionSelect.value == "deny") {
        showNum();
    }
})
function showText() {
    //these variables need enaming
    numDiv.style.display = "none";
    textDiv.style.display = "";
}
function showNum() {
    numDiv.style.display = "";
    textDiv.style.display = "none";
}
function showNone() {
    numDiv.style.display = "none";
    textDiv.style.display = "none";
}


// let updateButton = document.getElementById("updateButton");
// updateButton.addEventListener("click", function (event) {
//     event.preventDefault();
//     //this needs to updateaccounts and selectActions...
//     console.log("accountSelect");
//     //first set url based on what method being used...
//     //ifs...
//     console.log(actionSelect);
//     let fetchUrl = baseUrl;
//     //VALIDATE THE AMOUNT BEFORE RUNNINGTHIS
//     if (amountInput.value > 0) {
//         bodyWarning.style.display = "none";
//         if (actionSelect.value == "withdraw") {
//             console.log("this will withdraw");
//             // url = baseUrl + 
//             fetchUrl += "/api/accounts/withdraw";
//             //fetch to change DB
//             fetch(fetchUrl, {
//                 method: "PUT",
//                 body: JSON.stringify({
//                     username: username,
//                     password: password,
//                     accountNumber: selectFrom.value,
//                     amount: amountInput.value
//                 }),
//                 headers: {
//                     "Content-type": "application/json; charset=UTF-8"
//                 }
//             })
//                 .then(res => res.json())
//                 .then(data => {
//                     if (data.error) {
//                         bodyWarning.innerHTML = "Withdrawal Failed";
//                         bodyWarning.style.display = "";
//                     }
//                     updateAccounts(); //gets new account info
//                 })
//                 .catch(err => {
//                     console.log(err);
//                     //display some sort of warning to user
//                     warning.innerHTML = "Failed to connect to server";
//                     warning.style.display = "";
//                     //need to figure out other possible errors
//                     updateAccounts(); //gets new account info
//                 });
//         }
//         else if (actionSelect.value == "deposit") {
//             console.log("this will deposit");
//             fetchUrl += "/api/accounts/deposit";
//             fetch(fetchUrl, {
//                 method: "PUT",
//                 body: JSON.stringify({
//                     username: username,
//                     password: password,
//                     accountNumber: selectTo.value,
//                     amount: amountInput.value
//                 }),
//                 headers: {
//                     "Content-type": "application/json; charset=UTF-8"
//                 }
//             })
//                 .then(res => res.json())
//                 .then(data => {
//                     if (data.error) {
//                         bodyWarning.innerHTML = "Deposit Failed";
//                         bodyWarning.style.display = "";
//                     }
//                     updateAccounts(); //gets new account info
//                 })
//                 .catch(err => {
//                     console.log(err);
//                     //display some sort of warning to user
//                     warning.innerHTML = "Failed to connect to server";
//                     warning.style.display = "";
//                     //need to figure out other possible errors
//                     updateAccounts(); //gets new account info
//                 });
//         }
//         else if (actionSelect.value == "transfer") {
//             console.log("this will transfer");
//             fetchUrl += "/api/accounts/transfer";
//             fetch(fetchUrl, {
//                 method: "PUT",
//                 body: JSON.stringify({
//                     username: username,
//                     password: password,
//                     accountNumberFrom: selectFrom.value,
//                     accountNumberTo: selectTo.value,
//                     amount: amountInput.value
//                 }),
//                 headers: {
//                     "Content-type": "application/json; charset=UTF-8"
//                 }
//             })
//                 .then(res => res.json())
//                 .then(data => {
//                     if (data.error) {
//                         bodyWarning.innerHTML = "transfer Failed";
//                         bodyWarning.style.display = "";
//                     }
//                     updateAccounts(); //gets new account info
//                 })
//                 .catch(err => {
//                     console.log(err);
//                     //display some sort of warning to user
//                     warning.innerHTML = "Failed to connect to server";
//                     warning.style.display = "";
//                     //need to figure out other possible errors
//                     updateAccounts(); //gets new account info
//                 });
//         }
//         else if (actionSelect.value == "apply") {
//             //apply for account
//             fetchUrl += "/api/accounts";
//             fetch(fetchUrl, {
//                 method: "POST",
//                 body: JSON.stringify({
//                     username: username,
//                     password: password,
//                     amount: amountInput.value
//                 }),
//                 headers: {
//                     "Content-type": "application/json; charset=UTF-8"
//                 }
//             })
//                 .then(res => res.json())
//                 .then(data => {
//                     console.log(data);
//                     if (data.error) {
//                         bodyWarning.innerHTML = "Application Failed";
//                         bodyWarning.style.display = "";
//                     }
//                     else if (data) {

//                         bodyWarning.innerHTML = "Application successful. Please allow up to 48 hours for approval";
//                         bodyWarning.style.display = "";
//                         updateAccounts(); //gets new account info
//                     }
//                     else {
//                         bodyWarning.innerHTML = "An error occurred. Application May Have Failed";
//                         bodyWarning.style.display = "";
//                     }
//                 })
//                 .catch(err => {
//                     console.log(err);
//                     //display some sort of warning to user
//                     warning.innerHTML = "Failed to connect to server";
//                     warning.style.display = "";
//                     //need to figure out other possible errors
//                     updateAccounts(); //gets new account info
//                 });
//         }
//         // add new actions /features here!
//         else if (actionSelect.value == "refresh") {
//             updateAccounts(); //gets new account info
//         }
//     }
//     else {
//         bodyWarning.innerHTML = "Amount must be greater than zero";
//         bodyWarning.style.display = "";
//     }

// });
// ///WIP function!
function updateAccounts() { //pass in url to fetch too
    //fetches from employee who is logged in
    let url = baseUrl + "/api/customer/" + customerInput.value;
    fetch(url)
        .then(res => res.json())
        .then(data => {
            if (data) {
                // hideLoginDiv(); //hides login and shows body
                let htmlStr = "";
                for (i = 0; i < data.accounts.length; i++) {
                    let account = data.accounts[i];
                    //set account info to a table row
                    htmlStr += `<tr><td>${account.accountNumber}</td><td>${account.balance}</td><td>${account.status}</td></tr>`;
                    //update select options with account numbers
                    if (account.status == "active") {
                        let ele = document.createElement("OPTION");
                        ele.value = data.accounts[i].accountNumber;
                        ele.innerText = "From Account Number: " + data.accounts[i].accountNumber;
                        selectFrom.appendChild(ele);
                        ele = document.createElement("OPTION");
                        ele.value = data.accounts[i].accountNumber;
                        ele.innerText = "Into Account Number: " + data.accounts[i].accountNumber;
                        selectTo.appendChild(ele);
                    }
                }
                let tableBody = document.getElementById("tableBody");
                tableBody.innerHTML = htmlStr; //will not work on IE...
            }
            else {
                warning.innerHTML = "Failed to connect to server";
                warning.style.display = "";
            }
        })
        .catch(err => {
            console.log(err);
            //display some sort of warning to user
            warning.innerHTML = "Failed to connect to server";
            warning.style.display = "";
            //need to figure out other possible errors
        });

}

//need to add approve method!!!!
//need to add section for approved by - no, leave that in employee
//need to build employee html and js...




function fetchResults(event) {
    event.preventDefault();
    if (actionSelect.value == "approve") {

    }
    else if (actionSelect.value == "deny") {

    }
    else if (actionSelect.value == "viewCustomer") {
        let url = baseUrl + "/api/customer/" + customerInput.value;
        fetch(url)
            .then(res => res.json())
            .then(data => {
                if (data) {
                    console.log(data);
                    // hideLoginDiv(); //hides login and shows body
                    let htmlStr = "";
                    let headStr = "<tr><th scope='col'>Account Number</th><th scope='col'>Balance</th><th scope='col'>Status</th><th scope='col'>Approval ID</th></thead>";
                    tableHead.innerHTML = headStr;
                    console.log.data;
                    for (i = 0; i < data.accounts.length; i++) {
                        let account = data.accounts[i];
                        //set account info to a table row
                        htmlStr += `<tr><td>${account.accountNumber}</td><td>${account.balance}</td><td>${account.status}</td></tr>`;
                        //update select options with account numbers
                        if (account.status == "active") {
                            let ele = document.createElement("OPTION");
                            ele.value = data.accounts[i].accountNumber;
                            ele.innerText = "From Account Number: " + data.accounts[i].accountNumber;
                            if(data.accounts[i].approvedByEmployeeId){
                                data+=data.accounts[i].approvedByEmployeeId;
                                //not appearing!?!
                            }  
                            resultsDiv.appendChild(ele);
                        }
                    }
                    let tableBody = document.getElementById("tableBody");
                    tableBody.innerHTML = htmlStr; //will not work on IE...
                }
                else {
                    warning.innerHTML = "Failed to connect to server";
                    warning.style.display = "";
                }
            })
            .catch(err => {
                console.log(err);
                //display some sort of warning to user
                warning.innerHTML = "Failed to connect to server";
                warning.style.display = "";
                //need to figure out other possible errors
            });
    }
    else if (actionSelect.value == "viewAccount") {
        let url = baseUrl + "/api/account/" + amountInput.value;
        fetch(url)
            .then(res => res.json())
            .then(data => {
                if (data) {
                    // hideLoginDiv(); //hides login and shows body
                    let htmlStr = "";
                    console.log.data;
                    for (i = 0; i < data.accounts.length; i++) {
                        let account = data.accounts[i];
                        //set account info to a table row
                        htmlStr += `<tr><td>${account.accountNumber}</td><td>${account.balance}</td><td>${account.status}</td></tr>`;
                        //update select options with account numbers
                        if (account.status == "active") {
                            let ele = document.createElement("OPTION");
                            ele.value = data.accounts[i].accountNumber;
                            ele.innerText = "From Account Number: " + data.accounts[i].accountNumber + data.account.approvedBy;
                            resultsDiv.appendChild(ele);
                        }
                    }
                    let tableBody = document.getElementById("tableBody");
                    tableBody.innerHTML = htmlStr; //will not work on IE...
                }
                else {
                    warning.innerHTML = "Failed to connect to server";
                    warning.style.display = "";
                }
            })
            .catch(err => {
                console.log(err);
                //display some sort of warning to user
                warning.innerHTML = "Failed to connect to server";
                warning.style.display = "";
                //need to figure out other possible errors
            });
    }


}