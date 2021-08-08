<?php

require 'dbConnection.php';

ob_clean();

$category = $_POST['category'];
$id = $_POST['id'];

$sql = "SELECT id FROM Category WHERE " . strtolower($category) . "id = ?;";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "i", $id);

if (mysqli_execute($stmt)) {	
	mysqli_stmt_bind_result($stmt, $catid);
	mysqli_stmt_fetch($stmt);
	echo $catid;
} else {
	echo "Failure";
}
 
mysqli_stmt_close($stmt);
