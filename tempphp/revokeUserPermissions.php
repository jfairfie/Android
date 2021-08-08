<?php

//Simply removes permissions to a user based on the table given, and the permissions 
require 'dbConnection.php';

ob_clean();

$sql = "REVOKE " .$_POST['permission'].  " ON " . $dbname  ." .$_POST['table']. " FROM '".$_POST['user'] . "'@'%'; ";

$stmt = mysqli_prepare($conn, $sql);

if (mysqli_execute($stmt)) {
	echo "Sucessfully removed permissions";
} else {
	echo "Unsuccessfully removed permissions";
}
