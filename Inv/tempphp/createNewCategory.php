<?php

//Creates a new Category ie. Creating a new table

require 'dbConnection.php';

//Accepts an array of new fields
$fields = $_POST['fields']; //Fields will also contain data type ex: name VARCHAR(100), ...

$category = $_POST['category'];

$sql = "CREATE TABLE " . $category . " (" . strtolower($category) . "id INT PRIMARY KEY AUTO_INCREMENT, " . $fields . ");";

$stmt = mysqli_prepare($conn, $sql);


if (mysqli_execute($stmt)) {
	echo "Successfully created table" . $category;
	mysqli_stmt_close($stmt);
	
	//Altering Category table, adding additional id	
	$sql = "ALTER TABLE Category ADD COLUMN (" . strtolower($category) . "id INT);";
	//echo $sql; 
	$stmt = mysqli_prepare($conn, $sql);	
	if (mysqli_execute($stmt)) {
		echo "Added " . strtolower($category) . "id to Category!";	
	} else {
		echo "Failed to add column to category";
	}
	mysqli_stmt_close($stmt);	
		

	//Adding foriegn constraint to Category
	$sql = "ALTER TABLE Category ADD CONSTRAINT " . strtolower($category) . "fk" . " FOREIGN KEY (" . strtolower($category) . "id) REFERENCES " . $category . "(" . strtolower($category) . "id);";
	$stmt = mysqli_prepare($conn, $sql);
	if (mysqli_execute($stmt)) {
		echo "Successfully added foreign constraint";
	} else {
		echo "Unsuccessfully added foreign constraint";
	}

	mysqli_stmt_close($stmt);
	//Adding tablename to Whitelist
	$sql = "INSERT INTO Whitelist (tablename) VALUES (?);";
	$stmt = mysqli_prepare($conn, $sql);
	mysqli_stmt_bind_param($stmt, "s", $category);
	if (mysqli_execute($stmt)) {
		echo "Sucessfully entered into Whitelist";
	} else {
		echo "Unsuccessfully entered into whitelist";
	}
	
} else {
	echo "Failed to create table " . $category;
} 

