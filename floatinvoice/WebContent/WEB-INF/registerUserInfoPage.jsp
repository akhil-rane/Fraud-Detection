<!-- index.html -->
<!DOCTYPE html>
<html ng-app="validationApp" >
<head>
    <!-- CSS ===================== -->
    <!-- load bootstrap -->
    <link rel="stylesheet" href="../css/bootstrap.min.css"> 
    <link rel="stylesheet" href="../css/angular-material.min.css">
    <style>
        body    { padding-top:30px; }
    </style>
    
    <!-- JS ===================== -->
    <!-- load angular -->
<script src="../js/angular.min.js"></script>
<script src="../js/angular-animate.min.js"></script>
<script src="../js/angular-aria.min.js"></script>
<script src="../js/angular-messages.min.js"></script>
<script src="../js/angular-material.min.js"></script>
      <script>
        // create angular app
        var validationApp = angular.module('validationApp', ['ngMaterial', 'ngMessages']);

        validationApp.config(function($mdThemingProvider) {
          $mdThemingProvider.theme('default')
            .primaryPalette('light-blue')
            .accentPalette('green');
        });
        // create angular controller
        validationApp.controller('mainController', function($scope, $window, $http) {
            $scope.user= {};
            // function to submit the form after all validation has occurred            
            $scope.submitForm = function() {
                // check to make sure the form is completely valid
                if ($scope.userForm.$valid) {
                    
                    $http({
                        method:'POST',
                        url:'/floatinvoice/register/usrInfo',
                        data:$scope.user,
                        xhrFields: {
                            withCredentials: true
                        },
                        headers:{'Content-Type':'application/json'}
                        }).then(function successCallback(response) {
                            $window.location.replace('/floatinvoice/welcomePage');
                            console.log(response);
                          }, function errorCallback(response) {
                            console.log(response);
                      });
                }
            };
        });
    </script>
</head>

<!-- apply angular app and controller to our body -->
<body ng-controller="mainController" >
<div class="container">    
    <!-- PAGE HEADER -->
    <div class="col-sm-8 col-sm-offset-2">
    <img src = "../img/logo.jpg" height=70/>

    <div>
    <h2 class="page-header">Add Personal Information</h2>
    </div>
    <md-content layout-padding>
    <div>
      <form name="userForm" ng-submit="submitForm()" novalidate>
<!--         <div layout-gt-xs="row">
          <md-input-container class="md-block" flex-gt-xs>
            <label>Company (Disabled)</label>
            <input ng-model="user.company" disabled>
          </md-input-container>
          <md-datepicker ng-model="user.submissionDate" md-placeholder="Enter date">
          </md-datepicker>
        </div> -->
        <div layout-gt-xs="row"> 
          <md-input-container class="md-block" flex-gt-sm>
           <label>Director's First Name</label>
           <input type="text" name="directorFName" ng-model="user.directorFName"/>
          </md-input-container>
          <md-input-container class="md-block" flex-gt-sm>
           <label>Director's Last Name</label>
           <input type="text" name="directorLName"  ng-model="user.directorLName"/>
          </md-input-container>
        </div>
        <div layout-gt-xs="row">
         <md-input-container class="md-block" flex-gt-sm>
          <label>Pan Card Number</label>
          <input type="text" name="panCardNo" ng-model="user.panCardNo"/>         
         </md-input-container>
         <md-input-container class="md-block" flex-gt-sm>
          <label>Aadhar Card Number</label>
          <input type="text" name="aadharCardId" ng-model="user.aadharCardId"/>         
         </md-input-container>
        </div>    
       
        <md-input-container class="md-block" flex-gt-sm>
          <label>Bank Account Number</label>
          <input type="text" name="bankAcctNo" ng-model="user.bankAcctNo">
        </md-input-container>

        <div layout-gt-xs="row">
         <md-input-container class="md-block" flex-gt-sm>
          <label>Bank Name</label>
          <input type="text" name="bankName" ng-model="user.bankName"/>         
         </md-input-container>
         <md-input-container class="md-block" flex-gt-sm>
          <label>IFSC Code</label>
          <input type="text" name="ifscCode" ng-model="user.ifscCode"/>         
         </md-input-container>
        </div>    
        
           <md-button type="submit" class="md-raised md-primary" ng-disabled="userForm.$invalid">Submit</md-button> 
     </form>
    </div>
  </md-content>
    <!-- FORM -->
    <!-- pass in the variable if our form is valid or invalid -->

    <!-- <form name="userForm" ng-submit="submitForm()" novalidate>
         <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.directorFName.$invalid }">
            <label>Director First Name</label>
            <input type="text" name="directorFName" class="form-control" ng-model="user.directorFName">
        </div>  
        <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.directorLName.$invalid }">
            <label>Director Last Name</label>
            <input type="text" name="directorLName" class="form-control" ng-model="user.directorLName">
        </div> 
        <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.panCardNo.$invalid }">
            <label>Pan Card Number</label>
            <input type="text" name="panCardNo" class="form-control" ng-model="user.panCardNo">
        </div>  
        <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.aadharCardId.$invalid }">
            <label>Aadhar Card Id</label>
            <input type="text" name="aadharCardId" class="form-control" ng-model="user.aadharCardId">
        </div> 
        <div class="col-sm-8 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.bankAcctNo.$invalid }">
            <label>Bank Account Number</label>
            <input type="text" name="bankAcctNo" class="form-control" ng-model="user.bankAcctNo">            
        </div>
        <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.bankName.$invalid }">
            <label>Bank Name</label>
            <input type="text" name="bankName" class="form-control" ng-model="user.bankName">
        </div>  
        <div class="col-sm-3 col-sm-offset-2 form-group" ng-class="{ 'has-error' : userForm.ifscCode.$invalid }">
            <label>IFSC Code</label>
            <input type="text" name="ifscCode" class="form-control" ng-model="user.ifscCode">
        </div> 
        <br/>        
        <div class="col-sm-8 col-sm-offset-2">
            <button type="submit" class="btn btn-primary" ng-disabled="userForm.$invalid">Submit</button>
        </div>
    </form> -->
    </div>
</div><!-- /container -->
</body>
</html>