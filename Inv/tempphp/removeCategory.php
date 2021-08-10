<?php
require 'dbConnection.php';
ob_clean();

$category = $_POST['category'];

//Remove foreign constraint from Category 
$sql = "ALTER TABLE Category DROP CONSTRAINT " . strtolower($category) . "fk;"; 

$stmt = mysqli_prepare($conn, $sql);

if (mysqli_execute($stmt)) {
	echo "Removed foreign key successfully";

	mysqli_stmt_close($stmt);

	//Remove column [category]id from Category
	$sql = "ALTER TABLE Category DROP COLUMN " . strtolower($category) . "id;";
	$stmt = mysqli_prepare($conn, $sql);

	if (mysqli_execute($stmt)) {
		echo "Removed column from category successfully";
	} else {
		echo "Unsuccesffully removed column";
	}

	mysqli_stmt_close($stmt);

	//Dropping table [category]
	$sql = "DROP TABLE " . $category . ";";
	$stmt = mysqli_prepare($conn, $sql);

	if (mysqli_execute($stmt)) {
		echo "Successfully dropped table " . $category;
	} else {
		echo "Unsuccessfully dropped table " . $category; 
	}

	mysqli_stmt_close($stmt);

	//Remove record from whitelist
	$sql = "DELETE FROM Whitelist WHERE tablename=?;";
	$stmt = mysqli_prepare($conn, $sql);
	mysqli_stmt_bind_param($stmt, "s", $category);	

	if (mysqli_execute($stmt)) {
		echo "Successfully removed item from whitelist"; 
	} else {
		echo "Unsuccessfully removed item from whitelist";
	}

} else {
	echo "Unsuccessfully removed foreign key";
}

