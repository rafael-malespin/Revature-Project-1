

window.onload=function(){
    checkIfLoggedIn();

    document
        .getElementById("loginSubmit")
        .addEventListener('click',login);

}

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
                
            }
            else{
                let res = JSON.parse(xhttp.responseText);
                if(res.roleID==="EMPLOYEE"){
                    window.location.replace("dashboard.html");
                }
                else{
                    window.location.replace("manager-dash.html");
                }
            }
        }
        else if(xhttp.readyState===4 && xhttp.status ===404){
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
    document.getElementById("loginError").style.visibility="hidden"
    let xhttp=new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState===4 && xhttp.status===200){
            if(xhttp.responseText==="Logged In"){
                document.getElementById("loginError").style.visibility="hidden";
                window.location.href="dashboard.html";
            }
            else{
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
    xhttp.send(JSON.stringify(formToSend));
}