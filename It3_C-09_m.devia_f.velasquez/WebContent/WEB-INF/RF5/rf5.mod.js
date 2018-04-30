(function(ng) {
  var mod = angular.module('RF5Mod', ['ui.router']);

  mod.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    
	  $stateProvider.state('rf5', {
          views: {
              inputView: {
                  templateUrl: "RF5/rf5.input.html",
                  controller: "RF5Ctrl"
              }
          }
      }).state('rf5Response', {

          params: {
        	  	fechaInicio: null,
        		fechaFin: null,
        		fechaCreacionOferta: null,
        		tipoContrato : null,
        		numPersonas : null,
        		alojamiento  : null,
        		cliente  : null
          },

          views: {
              inputView: {
                  templateUrl: "RF5/rf5.input.html"
              },
              outputView: {
                  templateUrl: "RF5/rf5.output.html",
                  controller: "responseRF5Ctrl"
              }
          }
      });
  }]);

})(window.angular);