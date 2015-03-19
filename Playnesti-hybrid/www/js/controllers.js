angular.module('playnesti.controllers', [])

    .controller('MenuCtrl', function($scope,$location) {
        $scope.getClass = function(path) {
            if ($location.path().substr(0, path.length) == path) {
                return "active"
            } else {
                return ""
            }
        }
    })


    .controller('ProgramCtrl', function($scope, $ionicLoading, $ionicScrollDelegate, getData) {

        //Filter
        $scope.filterProgram = [
            { label: 'Programa', filter: 'Programa', value: '' },
            { label: 'Quarta, 18', filter: 'Dia 18', value: 18 },
            { label: 'Quinta, 19', filter: 'Dia 19', value: 19 },
            { label: 'Sexta, 20', filter: 'Dia 20', value: 20 },
            { label: 'Sabado, 21', filter: 'Dia 21', value: 21 },
            { label: 'Domingo, 22', filter: 'Dia 22', value: 22 }
        ];
        $scope.getFormatedDay = function(day){
            var divider = false;
            angular.forEach($scope.filterProgram, function(value, key) {
                if(day == value.value){
                    divider = value.label;
                    return;
                }
            });
            return divider;
        };

        $scope.getData = function(params){
            $ionicLoading.show({
                content: 'Loading',
                animation: 'fade-in',
                showBackdrop: true,
                maxWidth: 200,
                showDelay: 0
            });
            getData(params, function(data){
                var program = [];
                var day = 0;
                angular.forEach(data, function(value, key) {
                    var dividerLabel = $scope.getFormatedDay(value.day);
                    // Add divider
                    if(day != value.day){
                        day = value.day;
                        this.push({
                            divider: true,
                            label: dividerLabel
                        });
                    }
                    // Add Content
                    this.push({
                        title: value.title,
                        subTitle: value.subtitle,
                        thumbnailSrc: value.thumbnailSrc,
                        schedule: value.schedule,
                        location: value.location,
                        speaker: value.speaker
                    });
                }, program);
                $scope.programs = program;
            });

            $ionicLoading.hide();
        };

        $scope.update = function(item) {
            var params = 'schedule';
            if(angular.isNumber(item.value)){
                params = params + '/'+item.value
            }
            $ionicScrollDelegate.scrollTop();
            $scope.programs = [];
            $scope.getData(params);
            console.log(item.label);
            $scope.programSelected = {label: item.label, value: item.value};
        };
        $scope.programSelected = {label: 'Program', value: ''};
        $scope.getData('schedule');
    })

    .controller('ActivitiesCtrl', function($scope, $ionicLoading, getData) {
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        getData('activities', function(data){
            $scope.activities = data;
        });
        $ionicLoading.hide();

    })

    .controller('LanPartyCtrl', function($scope, $ionicLoading, getData) {
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        getData('lanparty', function(data){
            $scope.lanparty = data;
        });
        $ionicLoading.hide();

    })

    .controller('GalleryCtrl', function($scope, $ionicLoading, getData) {
        $ionicLoading.show({
            content: 'Loading',
            animation: 'fade-in',
            showBackdrop: true,
            maxWidth: 200,
            showDelay: 0
        });
        getData('gallery', function(data){
            console.log(data);
            $scope.gallery = data;
            $scope.test = 'test';
        });
        $ionicLoading.hide();

    })

