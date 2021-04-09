

window.onload=function(){
    checkIfLoggedIn();

    document
        .getElementById("loginSubmit")
        .addEventListener('click',login);

}

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
                
            }
            else{
                let res = JSON.parse(xhttp.responseText);
                console.log(res);
                if(res.roleID==="EMPLOYEE"){
                    console.log("going to employee dash")
                    window.location.replace("dashboard.html");
                }
                else{
                    console.log("going to manager dash1");
                    window.location.replace("manager-dash.html");
                }
            }
        }
        else if(xhttp.readyState===4 && xhttp.status ===404){
            console.log(xhttp.responseText);
        }
    }

    xhttp.open("GET","http://localhost:9003/api/users/login");
    xhttp.send();
}

function login(eve){
    eve.preventDefault();
    attemptLogin();
}
function attemptLogin(){
    console.log("Trying to log in");
    document.getElementById("loginError").style.visibility="hidden"
    let xhttp=new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState===4 && xhttp.status===200){
            console.log("Got Response");
            if(xhttp.responseText==="Logged In"){
                console.log("login in")
                document.getElementById("loginError").style.visibility="hidden";
                window.location.href="dashboard.html";
            }
            else{
                console.log("Login failed")
            document.getElementById("loginError").style.visibility="visible"
            }
        }
    }

    xhttp.open("POST","http://localhost:9003/api/users/login");
    xhttp.setRequestHeader( 'Content-Type', 'application/json');

    let user = document.getElementById("userName1").value;
    let pass = document.getElementById("password1").value;
    let formToSend={
        "username":user,
        "password":pass
    }
    console.log(formToSend);
    xhttp.send(JSON.stringify(formToSend));
}