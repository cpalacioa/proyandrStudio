<?php
 
require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';
 \Slim\Slim::registerAutoloader();
 // Creamos la aplicación.
$app = new \Slim\Slim();
// Indicamos el tipo de contenido y condificación que devolvemos desde el framework Slim.
#$app->contentType('text/html; charset=utf-8');

/**
 * Verificamos los parametros requeridos
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
 
    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(500, $response);
        $app->stop();
    }
}

/**
 * Validar correos
 */
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = 'Email address is not valid';
        echoRespnse(400, $response);
        $app->stop();
    }
}

function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);
 
    // setting response content type to json
    $app->contentType('application/json');
 
    echo json_encode($response);
}
 
/**
 * User Registration
 * url - /register
 * method - POST
 */
$app->post('/user/register', function() use ($app) {
            // check for required params
           verifyRequiredParams(array('username', 'email', 'aplication','identityprovider'));
 
            $response = array();
 
            // reading post params
            $username = $app->request->post('username');
            $email = $app->request->post('email');
            $aplication = $app->request->post('aplication');
            $provider=$app->request->post('identityprovider');
 
            // validating email address
            validateEmail($email);
 
            $db = new DbHandler();
            $res = $db->createUser($username,$email,$aplication,$provider);
 
            if ($res == USER_CREATED_SUCCESSFULLY) {
                $response["error"] = false;
                $response["message"] = "You are successfully registered";
                echoRespnse(201, $response);
            } else if ($res == USER_CREATE_FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(200, $response);
            } else if ($res == USER_ALREADY_EXISTED) {
                $response["error"] = true;
                $response["message"] = "Sorry, this email already existed";
                echoRespnse(200, $response);
            }
        });
/* guardar un dispositivo*/
$app->post('/device/register', function() use ($app) {
            // check for required params
           verifyRequiredParams(array('serial', 'aplication', 'user'));
            $response = array();
            // reading post params
            $serial = $app->request->post('serial');
            $user = $app->request->post('user');
            $aplication = $app->request->post('aplication');
            $db = new DbHandler();
            $res=$db->InsertDevice($serial, $aplication, $user);
 
            if ($res == SUCCESSFULLY) {
                $response["error"] = false;
                $response["message"] = "device successfully registered";
                echoRespnse(201, $response);
            } else if ($res == FAILED) {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while registereing";
                echoRespnse(200, $response);
            }
        });
        
$app->get('/user/getbyparams',  function () use ($app) {
          $email = $app->request()->params('email');
          $aplicacion = $app->request()->params('aplication');
          $provider = $app->request()->params('provider');
          
            $response = array();
            $db = new DbHandler();
            $result =$db->getUser($email, $aplicacion, $provider);
            if ($result != NULL) {
                $response["error"] = false;
                $response["id"] = $result["Id"];
                $response["username"] = $result["username"];
                $response["email"] = $result["email"];
                echoRespnse(200, $response);
            } else {
                $response["error"] = true;
                $response["message"] = "User doesn't exists";
                
                echoRespnse(404, $response);
            }
});        
        
$app->run();

        ?>