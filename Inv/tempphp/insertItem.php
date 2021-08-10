<?php

require 'config.php';
require 'returnUserId.php';
require 'returnColumnNames.php';
require 'dbConnection.php';

ob_clean();

$field = substr($field, strpos($field, ",")+1); //Removing id from insert, as id is inserted as NULL
$fieldFormat = "(%s)"; 
$fieldFormat = sprintf($fieldFormat, $field);

//Params from user input
$params = $_POST['params'];
$params = explode(",", $params);

$field = explode(",", $field);

//Getting number of params for INSERT query
$value = "(";
for ($i = 0; $i < count($field); $i++) {
	$value .= "?";
	if ($i < count($field)-1) {
		$value .= ",";
	}
}
$value .= ")";

$sql = "INSERT INTO " . $category . " " . $fieldFormat . " VALUES " . $value . ";";

$stmt = mysqli_prepare($conn, $sql);
$stmt = DynamicBindVariables($stmt, $params);

if (mysqli_execute($stmt)) {
	echo "Inserted Successfully";
	mysqli_stmt_close($stmt);
	$lastId = mysqli_insert_id($conn);	

	//Adding data into category 
	$sql = "INSERT INTO Category (" . strtolower($category) . "id) VALUES(" . $lastId . ");";
	$stmt = mysqli_prepare($conn, $sql);
	if (mysqli_execute($stmt)) {
		echo "Inserted new category successfully";
	}

	//Adding data into entry 
	mysqli_stmt_close($stmt);
	$lastId = mysqli_insert_id($conn);
	$sql = "INSERT INTO Entry (id, quantity, date, userid) VALUES (" . $lastId . ", 0, current_date()," . $userid . ");";
	$stmt = mysqli_prepare($conn, $sql);
	if (mysqli_execute($stmt)) {
		echo "Inserted into Entry successfully";
	}
} else {
	echo "Failure to insert";
}

//Dynamically binding to $stmt 
function DynamicBindVariables($stmt, $params)
{
    if ($params != null)
    {
        // Generate the Type String (eg: 'issisd')
        $types = '';
        foreach($params as $param)
        {
            if(is_int($param)) {
                // Integer
                $types .= 'i';
            } elseif (is_float($param)) {
                // Double
                $types .= 'd';
            } elseif (is_string($param)) {
                // String
                $types .= 's';
            } else {
                // Blob and Unknown
                $types .= 'b';
            }
        }
  
        // Add the Type String as the first Parameter
        $bind_names[] = $types;
  
        // Loop thru the given Parameters
        for ($i=0; $i<count($params);$i++)
        {
            // Create a variable Name
            $bind_name = 'bind' . $i;
            // Add the Parameter to the variable Variable
            $$bind_name = $params[$i];
            // Associate the Variable as an Element in the Array
            $bind_names[] = &$$bind_name;
        }
         
        // Call the Function bind_param with dynamic Parameters
        call_user_func_array(array($stmt,'bind_param'), $bind_names);
    }
    
    return $stmt;
}
