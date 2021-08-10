<?php

//Simply grants permissions to a user based on the table given, and the permissions 
require 'dbConnection.php';

ob_clean();

$sql = "GRANT " .$_POST['permission'].  " ON  " . $dbname ." .$_POST['table']. " TO '".$_POST['user'] . "'@'%'; ";

$stmt = mysqli_prepare($conn, $sql);

if (mysqli_execute($stmt)) {
	echo "Sucessfully added permissions";
} else {
	echo "Unsuccessfully added permissions";
}
