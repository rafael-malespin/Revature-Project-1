
window.onload=function(){
    checkIfLoggedIn()

    document
        .getElementById("logoutButton")
        .addEventListener("click",logout)
    document
        .getElementById("allSelection")
        .addEventListener("click",selectionDropCallback("All"));
    document
        .getElementById("pendingSelection")
        .addEventListener("click",selectionDropCallback("PENDING"));
    document
        .getElementById("approvedSelection")
        .addEventListener("click",selectionDropCallback("APPROVED"));
    document
        .getElementById("deniedSelection")
        .addEventListener("click",selectionDropCallback("DENIED"));
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
                //do nothing
                window.location.replace("index.html");
            }
            else{
                let user = JSON.parse(xhttp.responseText);
                loggedInUser=user;
                if(user.roleID==="EMPLOYEE"){
                    window.location.replace("dashboard.html");
                }
                else{
                    WelcomeDOM(user)
                    loadReimbursements("All");
                }
                //TODO:set the dropdown label as All
            }
        }
    }

    xhttp.open("GET","http://localhost:9003/api/users/login");
    xhttp.send();
}

function WelcomeDOM(ourMessage){
    let welcomeBox= document.getElementById("welcomeBox");
    welcomeBox.innerText=ourMessage.firstName+" "+ourMessage.lastName+", "+ourMessage.roleID;
}

function loadReimbursements(selection){
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange= function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            
            //check if response is null
            if(xhttp.responseText===""){
                //do nothing
            }
            else if (xhttp.responseText==="File Not Found"){
                //do nothing
                //window.location.replace("index.html");
            }
            else{
                let json = JSON.parse(xhttp.responseText);
                if(json.length===0){
                    document.getElementById("pendingTable").style.visibility = "hidden";
                    document.getElementById("reimbursementTableBody").innerHTML="";
                    document.getElementById("pendingMessage").innerText="No reimbursements currently.";
                }
                else{
                    //populate with table information
                        createReimbursementTable(json);
                }
            }
        }
    }
    if(selection==="All"){
        xhttp.open("GET",`http://localhost:9003/api/reimbursements`);
    }
    else{
        xhttp.open("GET",`http://localhost:9003/api/reimbursements/${selection}`);
    }
    xhttp.send();
}
function createReimbursementTable(json){
    //cleaning up previous elements
    document.getElementById("pendingMessage").innerText="";
    document.getElementById("reimbursementTableBody").innerHTML="";
    //getting the table and making it visible
    let theTable=document.getElementById("pendingTable");
    theTable.style.visibility = "visible";
    //getting the body of the table
    let theBody=document.getElementById("reimbursementTableBody");
    for(let i = 0; i<json.length; i++){
        //making the cells of the current row
        let row = theBody.insertRow(i);
        let col1= row.insertCell(0);
        let col2= row.insertCell(1);
        let col3= row.insertCell(2);
        let col4= row.insertCell(3);
        let col5= row.insertCell(4);
        let col6= row.insertCell(5);
        let col7= row.insertCell(6);
        let col8= row.insertCell(7);
        let col9= row.insertCell(8);
        let col10=row.insertCell(9);
        col1.innerText=json[i].reimbursementID;
        col2.innerText="$"+json[i].amount;
        col3.innerText=json[i].type;
        col4.innerText=json[i].description;
        col5.innerText=json[i].authorName;
        let submit=new Date(json[i].submittedTime);
        col6.innerText= submit.toLocaleString();
        col7.innerText=json[i].resolverName;
        if(json[i].resolvedTime!==0 && json[i].resolvedTime!==null){
            submit=new Date(json[i].resolvedTime);
            col8.innerText=submit.toLocaleString();
        }
        else{
            submit=null;
            col8.innerText=submit;
        }
        col9.innerText=json[i].status;

        //trying to add the buttons for approval/denial only for pending reimbursements
        if(json[i].status==="PENDING"){
            addTwoButton(col10,json[i].reimbursementID);
        }
    }

}
function addTwoButton(cellVal,reimbursementID){
    var button1=document.createElement('button');
    var span1 = document.createElement('span');
    button1.setAttribute('class','btn btn-primary');
    button1.setAttribute('type','button');
    button1.setAttribute('style','width:100%')
    span1.innerHTML="Approve";
    var button2=document.createElement('button');
    var span2 = document.createElement('span');
    button2.setAttribute('class','btn btn-danger');
    button2.setAttribute('type','button');
    button2.setAttribute('style','width:100%; height:50%')
    span2.innerHTML="Deny"
    button1.appendChild(span1);
    button2.appendChild(span2);

    cellVal.appendChild(button1);
    cellVal.appendChild(button2);

    button1.addEventListener("click",callBackForApprovalButtons(reimbursementID,"APPROVED",loggedInUser.userID));
    button2.addEventListener('click',callBackForApprovalButtons(reimbursementID,"DENIED",loggedInUser.userID));
}



function callBackForApprovalButtons(reimbursementID,approval,userID){
    return function(){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(xhttp.readyState===4 && xhttp.status===200){
                if(xhttp.responseText==="Not Authorized"){
                    window.location.replace("index.html")
                }
                else if(xhttp.responseText==="invalid input"){
                }
                else if(xhttp.responseText==="Reimbursement was not updated"){
                    document.getElementById("updateError").style.visibility="visible";
                }
                else if(xhttp.responseText==="Reimbursement successfully updated."){
                    //reload the page/table contents
                    document.getElementById("updateError").style.visibility="hidden";
                    window.location.reload();
                }

            }
        }

        xhttp.open("PUT",`http://localhost:9003/api/reimbursements`);
        xhttp.setRequestHeader( 'Content-Type', 'application/json');
        let resolvedTime=Date.now();

        let formToSend={
            "resolvedTime" : resolvedTime,
            "resolverID" : userID,
            "reimbursementID":reimbursementID,
            "status" : approval
        }
        xhttp.send(JSON.stringify(formToSend));
    }

}

function logout(eve){
    eve.preventDefault();
    logout1();
}
function logout1(){
    let xhttp= new XMLHttpRequest();

    xhttp.onreadystatechange= function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            if(xhttp.responseText==="You have been logged out."){
                loggedInUser=null;
                window.location.replace("index.html");
            }
            
        }
    }
    xhttp.open("GET",`http://localhost:9003/api/users/logout`);
    xhttp.send();
}


function selectionDropCallback(selection){
    return function(){
        loadReimbursements(selection);
    }
}