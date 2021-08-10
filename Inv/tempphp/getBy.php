<?php
require 'dbConnection.php';
require 'returnColumnNames.php';

ob_end_clean();

$params = explode(",", $_POST["params"]);
$keys = explode(",", $_POST["keys"]);

$whereValues = "";

for ($i = 0; $i < count($params); $i++) {
	$whereValues .= $keys[$i] . "=?"; 
	if ($i < count($params)-1) {
		$whereValues .= " AND ";
	}
}

$sql = "SELECT " . $category . "." . $field . ",SUM(quantity) AS quantity FROM " . $category
. " INNER JOIN Category ON Category." . strtolower($category) . "id" . " = " . $category .
"." . strtolower($category) . "id" . " INNER JOIN Entry ON Entry.id = Category.id " 
. " WHERE " . $whereValues . " GROUP BY ". $category . "." . strtolower($category) . "id;";

$stmt = mysqli_prepare($conn, $sql); 
$stmt = DynamicBindVariables($stmt, $params);

if (mysqli_stmt_execute($stmt)) {
	//Delimeter, making an array of field names
	$field = explode(",", $field);
	array_push($field, "quantity");
	$returnField = array();
	
	//Displaying results from sql query
	$result = mysqli_stmt_get_result($stmt);
	while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
		$addArray = array();
		for ($i = 0; $i < count($field); $i++) {
			$tempArray = array($field[$i] => $row[$i]);
			$addArray = array_merge($addArray, $tempArray);	
		}
		$returnField[] = $addArray;
	}
	echo json_encode($returnField);

} else {
	echo "failed";
}

//Function binds dynamic variables 
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
