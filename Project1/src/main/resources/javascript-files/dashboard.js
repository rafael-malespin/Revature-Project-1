

window.onload=function(){
    checkIfLoggedIn()

    document
        .getElementById("logoutButton")
        .addEventListener("click",logout);
    document
        .getElementById("allSelection")
        .addEventListener("click",dropdownSelectionCallback("All"));
    document
        .getElementById("pendingSelection")
        .addEventListener("click",dropdownSelectionCallback("PENDING"));
    document
        .getElementById("approvedSelection")
        .addEventListener("click",dropdownSelectionCallback("APPROVED"));
    document
        .getElementById("deniedSelection")
        .addEventListener("click",dropdownSelectionCallback("DENIED"));
}

let loggedInUser;

function checkIfLoggedIn(){

    let xhttp= new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        
        if(xhttp.readyState===4 && xhttp.status ===200){
            
            //check if response is null
            if(xhttp.responseText===""){
                //do nothing
            }
            else if (xhttp.responseText==="No one is logged in"){
                //do nothing except go to login page
                window.location.replace("index.html");
            }
            else{
                loggedInUser= JSON.parse(xhttp.responseText);
                if(loggedInUser.roleID==="FINANCE_MANAGER"){
                    window.location.replace("manager-dash.html");
                }
                else{
                    WelcomeDOM()
                    loadPrelimReimbursements("All");
                }
            }
        }
    }

    xhttp.open("GET","http://localhost:9003/api/users/login");
    xhttp.send();
}

function WelcomeDOM(){
    let welcomeBox= document.getElementById("welcomeBox");
    welcomeBox.innerText=loggedInUser.firstName+" "+loggedInUser.lastName+", "+loggedInUser.roleID;
}

function loadPrelimReimbursements(selection){
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange= function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            
            //check if response is null
            if(xhttp.responseText===""){
                //do nothing
            }
            else if (xhttp.responseText==="File Not Found"){
                //do nothing
                window.location.replace("index.html");
            }
            else{
                let json = JSON.parse(xhttp.responseText);
                if(json.length===0){
                    document.getElementById("pendingTable").style.visibility = "hidden";
                    document.getElementById("reimbursementTableBody").innerHTML = "";
                    document.getElementById("pendingMessage").innerText=`No reimbursements currently with status: ${selection}.`;
                }
                else{
                    buildReimbursementTable(json);
                }
            }
        }
    }
    if(selection==="All"){
        xhttp.open("GET",`http://localhost:9003/api/user-reimbursements/${loggedInUser.userID}`);
    }
    else{
        xhttp.open("GET",`http://localhost:9003/api/user-reimbursements/${loggedInUser.userID}/${selection}`);
    }
    xhttp.send();
}

function buildReimbursementTable(reimbursements){
    //populate with table information
    document.getElementById("pendingMessage").innerText="";
    document.getElementById("reimbursementTableBody").innerHTML = "";
    //making the table visible
    let theTable=document.getElementById("pendingTable");
    theTable.style.visibility = "visible";
    //getting the body of the table
    let theBody=document.getElementById("reimbursementTableBody");
    for(let i = 0; i<reimbursements.length; i++){
        //making the cells of the current row
        let row = theBody.insertRow(i);
        let col1 = row.insertCell(0);
        let col2 = row.insertCell(1);
        let col3 = row.insertCell(2);
        let col4 = row.insertCell(3);
        let col5 = row.insertCell(4);
        let col6 = row.insertCell(5);
        let col7 = row.insertCell(6);
        let col8 = row.insertCell(7);
        let col9 = row.insertCell(8);
        //giving the cells values
        col1.innerText = reimbursements[i].reimbursementID;
        col2.innerText = "$"+reimbursements[i].amount;
        col3.innerText = reimbursements[i].type;
        col4.innerText = reimbursements[i].description;
        col5.innerText = reimbursements[i].authorName;
        let submit = new Date(reimbursements[i].submittedTime);
        col6.innerText = submit.toLocaleString();
        col7.innerText = reimbursements[i].resolverName;
        //if it is resolved, convert to useable string else set it to null
        if(reimbursements[i].resolvedTime !== 0 && reimbursements[i].resolvedTime !== null){
            submit = new Date(reimbursements[i].resolvedTime);
            col8.innerText = submit.toLocaleString();
        }
        else{
            col8.innerText = null;
        }
        col9.innerText = reimbursements[i].status;
    }
}

function logout(eve){
    eve.preventDefault()
    logout1()
}
function logout1(){
    let xhttp= new XMLHttpRequest();

    xhttp.onreadystatechange= function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            if(xhttp.responseText==="You have been logged out."){
                window.location.replace("/index.html");
                loggedInUser=null;
            }
            
        }
    }
    xhttp.open("GET",`http://localhost:9003/api/users/logout`);
    xhttp.send();
}

function dropdownSelectionCallback(selection){
    return function(){
        loadPrelimReimbursements(selection);
    }
}