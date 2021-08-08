<?php
require 'dbConnection.php';
require 'returnUserId.php';
require 'returnCategoryId.php';

ob_end_clean();
	
$quantity = $_POST['quantity'];
$date = date('Y-m-d');

$sql = "INSERT INTO Entry (id, quantity, date, userid) VALUES (?, ?, ?, ?);";
$stmt = mysqli_prepare($conn, $sql);

mysqli_stmt_bind_param($stmt, "iisi", $catid, $quantity, $date, intval($userid));

if (mysqli_stmt_execute($stmt)) {
	echo "Successfully executed";
} else {
	echo "Unsuccessfully executed";
}

mysqli_close($conn);
