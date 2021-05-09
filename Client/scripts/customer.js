








//refreshBusinessDiv should be called 
//to update account info when accounts


let nameSpan = document.getElementById("nameSpan");
let tableBody = document.getElementById("tableBody");
let actionSelect = document.getElementById("actionSelect");
let accountSelect = document.getElementById("accountSelect");
let selectFrom = document.getElementById("selectFrom");
let selectTo = document.getElementById("selectTo");


//reconfiguring so that signup/login is customer only





// things a customer can do on this page
// 1. apply for new account
// 2. see accounts... automatic?
// 3. withdraw
// 4. deposit
// 5. transfer
/*
    how to deal with these actions...
        apply for account should be in a menu...
        account number and balance auto shown

        withdraw, deposit, transfer
            option in a select to choose action-withdraw,etc
            clicking an option should change available inputs
            - from, to
            -should inputs select from available accounts?
            - or should people type in number manually...
            - perhaps "other" in "to" option...
        
*/


//not sure how to do accept a transfer...


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

//now populate options for from and to as other, <eachacc...>
