(function(ng) {
  var mod = angular.module('RF9Mod', ['ui.router']);

  mod.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    
    $stateProvider.state('rf9', {
        views: {
          inputView: {
            templateUrl: "RF9/rf9.input.html",
            controller: "RF9Ctrl"
          }
        }
      })
      .state('rf9Response', {
        
    	  params: {
              idAl: null,
              fechaCreacion: null,
              token: null
          },
    	  
    	  views: {
          inputView: {
            templateUrl: "RF9/rf9.input.html"
          },
          outputView: {
            templateUrl: "RF9/rf9.output.html",
            controller: "rf9ResponseCtrl"
          }
        }
      })
  }]);

})(window.angular);