# teamItestRepo
Sprint 2:
Test Login and Password:
admintest
passwd

Completed Features
Stats page
Profile page
Pet score and level system
Dynamic progress bar for timers

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Sprint 1:

Team I
Andreas, Eric, Itel, Johnny, Timothy

Completed Features:
Opening Screen including name, pet name, goal, and pet type selection options with error-checking
Home Page including Appbar
Create an Event Page including time dial, name, and description with error-checking
Edit Event Page
Delete Event
Timer/Stopwatch including start, pause, and exit functionality
New Tag Functionality including error-checking

Uncompleted Features:
Pet System
Profile
Statistics

General State:
When you enter the app for the first time you’ll be asked information like your name, your pet’s name, what type of pet you want, and your goal. After entering that information you’ll be guided to our main activity and can see three screens with the create event screen mostly implemented. You can access the Create Event feature from this screen and from there the timer/stopwatch. You also can edit an event once its been created. No special login information is needed to access the app. After the first time use of the app, you’ll still see the get started screen but will be navigated to the main activity instead of the information pages. The Analytics and profile page aren’t implemented and we have holders there for now.

Create new event
When the user clicks the FAB (Floating Action Button) on the home page, they are directed to the create new event page. In this page, they can set a name for the new task, switch between the task being a timer or a stopwatch, set a time duration if the event is a timer, choosing a tag for the event out of the four existing tags with unique icons (break, study, gaming and workout) as well as writing a description for the event. Error handling includes when the user didn’t specify the name of the event, not choosing a tag and setting the time duration to 0 seconds in timer mode.

Edit new event
With a layout similar to create new event page, the user is able to change the name of an existing event, re-selecting a tag, change the duration of the event as well as writing a new description. The user is prevented from switching whether the event is a timer or a stopwatch. 

Home page
On the home page, there are two main parts, the to do list and the finished tasks list, the latter is collapsed at the bottom of the screen and will be visible once the user clicks the button on its header. Upon creating an event, it will be automatically put into the to do list alongside with information such like its name, description, tag, whether it is a timer or a stopwatch, and the time left (or the time spent depending on how much time has passed) for the task. Next to each task is the button that would open up a context menu that allows the user to either edit (which takes the user to the edit new event menu) or delete the event. Upon the completion of each event (clicking the end button in timer or stopwatch or waiting for the time to run out in timer), the task will be put under the finished task category and there will be no option to delete or edit the event. On the top of the screen is the title bar that has LifePal’s logo as well as a status bar for the current pet.

Timer
Upon launching, the timer will wait for the user to manually start the countdown by pressing the start button. After which the start button will be turned into the pause button which the user can press to pause the time. The user can also click the end button on the other side to manually complete the event when the time hasn’t run out and put the task under the finished tasks category. On the top of the screen it shows the current event’s title and description and under the remaining time is the current time past. If the user want to continue using the timer later, they can click the back button and return to the main menu with the task in the to do list. 

Stopwatch
Largely similar to the timer but the time starts from 0.
