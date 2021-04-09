window.onload = function(){
    checkIfLoggedIn();

    document
        .getElementById("reimbursementSubmit")
        .addEventListener('click',submitCaller);
    
    var radios = document.querySelectorAll('input[type=radio][name="Type"');
    radios.forEach(radio=> radio.addEventListener('change',()=>currentType=radio.value));

    document.getElementById("amountInput")
        .addEventListener('change',()=>{
            this.value=(parseFloat(this.value)).toFixed(2);
        });

    document
        .getElementById("logoutButton")
        .addEventListener("click",logout)
}
let loggedInUser=null;
let currentType=null;

function checkIfLoggedIn(){
    console.log("Checking if Logged in");

    let xhttp= new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            console.log("Got Response.")
            
            //check if response is null
            if(xhttp.responseText===""){
                //do nothing
            }
            else if (xhttp.responseText==="No one is logged in"){
                //do nothing
                console.log(xhttp.responseText);
                window.location.replace("index.html");
            }
            else{
                loggedInUser = JSON.parse(xhttp.responseText);
                console.log(loggedInUser);
                if(loggedInUser.roleID==="FINANCE_MANAGER"){
                    console.log("going to manager dash2");
                    window.location.replace("manager-dash.html");
                }
            }
        }
    }

    xhttp.open("GET","http://localhost:9003/api/users/login");
    xhttp.send();
}

function submitCaller(eve){
    eve.preventDefault();
    submitNewReimbursement();
}

function submitNewReimbursement(){

    //exit the function if amount is nothing or invalid input
    if(document.getElementById("amountInput").value===""||document.getElementById("amountInput").value==="."){
        //TODO: make error message appear if this is reached
        document.getElementById("updateError").innerText="Invalid amount value.";
        document.getElementById("updateError").style.visibility="visible";
        return;
    }
    document.getElementById("updateError").style.visibility="hidden";
    document.getElementById("updateError").innerText="Reimbursement could not be submitted at this time.";
    
    let amountValue=Number.parseFloat(document.getElementById("amountInput").value);
    let descriptionValue=document.getElementById("descriptionInput").value;
    
    let xhttp=new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            console.log("Got Response.")

            if(xhttp.responseText==="Reimbursement successfully submitted."){
                window.location.href = "/dashboard.html"
            }
            else{
                document.getElementById("updateError").style.visibility="visible";
                document.getElementById("updateError").innerText="Reimbursement could not be submitted at this time.";
            }
            
        }
    }

    xhttp.open("POST","http://localhost:9003/api/reimbursements");
    xhttp.setRequestHeader( 'Content-Type', 'application/json');

    //need to get the variables from the fields
    if(currentType===null){
        currentType="OTHER";
    }
    

    let currentTime=Date.now();

    amountValue=Math.round((amountValue + Number.EPSILON) * 100) / 100
    let jsonToBeSent={
        "amount":amountValue,
        "submittedTime":currentTime,
        "description":descriptionValue,
        "authorID": loggedInUser.userID,
        "type":currentType
    }
    console.log(jsonToBeSent);;
    xhttp.send(JSON.stringify(jsonToBeSent))

}

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    if(charCode==46 && evt.srcElement.value.split('.').length>1){
        return false;
    }
    return true;
}
function logout(eve){
    eve.preventDefault();
    logout1();
}
function logout1(){
    let xhttp= new XMLHttpRequest();

    xhttp.onreadystatechange= function(){
        if(xhttp.readyState===4 && xhttp.status ===200){
            console.log("Got Response.")
            if(xhttp.responseText==="You have been logged out."){
                loggedInUser=null;
                window.location.replace("index.html");
            }
            
        }
    }
    xhttp.open("GET",`http://localhost:9003/api/users/logout`);
    xhttp.send();
}