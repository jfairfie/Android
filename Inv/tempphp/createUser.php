<?php

require 'dbConnection.php';

$sql = "CREATE USER '".$_POST['newUser']."'@'%' IDENTIFIED BY '".$_POST['newPass']."';";

echo $sql;

$stmt = mysqli_prepare($conn, $sql); 

//Entering new user into MySQL database 
if (mysqli_execute($stmt)) {
    echo "Successfully created user " . $_POST['newUser'];
    mysqli_stmt_close($stmt);

    //Inserting new user into the User database
    $sql = "INSERT INTO User (username, name) VALUES (?, ?);";
    
    $name = $_POST['name'];
    
    $stmt = mysqli_prepare($conn, $sql);
    mysqli_stmt_bind_param($stmt, "ss", $_POST['newUser'], $name);
    
    if (mysqli_execute($stmt)) {
        echo "Successfully entered user into database";
    } else {
        echo "Unsuccessfully entered user into database";
    }
    
    mysqli_stmt_close($stmt);
    
    //Granting User default priveleges
    $sqlList = array("GRANT INSERT, SELECT ON BPTInventory.Category TO '".$_POST['newUser'] . "'@'%';" ,
                     "GRANT INSERT, SELECT ON BPTInventory.Entry TO '".$_POST['newUser'] . "'@'%';" ,
		     "GRANT SELECT ON BPTInventory.User TO '".$_POST['newUser'] . "'@'%';" ,
                     "GRANT SELECT ON BPTInventory.Whitelist TO '".$_POST['newUser'] . "'@'%';");

    for ($i = 0; $i < count($sqlList); $i++) {
	$stmt = mysqli_prepare($conn, $sqlList[$i]);
    	mysqli_execute($stmt); 
    	mysqli_stmt_close($stmt);
    }
	
    
    mysqli_close($conn);
} else {
	echo "Unsuccessfully created user";
}
