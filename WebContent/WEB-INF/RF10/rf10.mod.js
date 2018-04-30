(function(ng) {
  var mod = angular.module('RF10Mod', ['ui.router']);

  mod.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    
    $stateProvider.state('rf10', {
        views: {
          inputView: {
            templateUrl: "RF10/rf10.input.html",
            controller: "RF10Ctrl"
          }
        }
      })
      .state('rf10Response', {
        
    	  params: {
              
    		  idAl: null,
              fechaCreacion: null,
              token: null,
              fechaRehabilitacion: null
          },
    	  
    	  views: {
          inputView: {
            templateUrl: "RF10/rf10.input.html"
          },
          outputView: {
            templateUrl: "RF10/rf10.output.html",
            controller: "rf10ResponseCtrl"
          }
        }
      })
  }]);

})(window.angular);