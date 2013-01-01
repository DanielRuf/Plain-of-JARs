$(document).ready(function(){
    $('#movie').jsMovie({
        sequence : "#FRAME_NAME#.jpg",    //the #### will be replaced with 0001,0002,0003,...
        folder   : "frames/",       //this is the path where the script can find the image sequence
        from     : #FRAME_FIRST#,               //the #### will start to replace with 0001
        to       : #FRAME_LAST#,              //the #### will start to replace with 0040
        width    : #FRAME_WIDTH#,             //the movie container will be resized to a width of 320px
        height   : #FRAME_HEIGHT#,             //the movie container will be resized to a height of 150px
        showPreLoader : true,       //we do want to see a preloader animation
        playOnLoad : false,         //we don't want to have the movie play after the images have been loaded automatically
        // the preloader animation is located in the folder "img/loader.png"
        // it is a 4x4 image matrix with each image of 40px by 40px
        loader   : {path:"img/loader.png",height:40,width:40,rows:4,columns:4} 
    });  
	$('#movie').jsMovie("play");
});