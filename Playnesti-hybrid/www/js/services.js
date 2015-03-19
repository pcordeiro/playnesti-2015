angular.module('playnesti.services', [])

    .factory('getData', function($http){
        return function(param, done) {
            console.log(param);
            $http({method: 'GET', url: 'http://private-d6e02-playnesti.apiary-mock.com/'+param+'?callback=JSON_CALLBACK'})
                .success(function(data, status, headers, config) {
                    done(data);
                })
                .error(function(data, status, headers, config) {
                    data = { "id": 1, "html": "<div> Error to retrieve schedule </div>"}
                    done(data);
                }
            );
        };
    }
);
