# teamItestRepo

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
Pet system
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

Profile:
When the user clicks the profile tab on the navigation bar, they see three dropdowns, the profile picture for the pet chosen by the user, and an ability to log out. The three dropdowns are personal information that the user filled out on the first opening of the app. First the pet dropdown which contains the pets name, pet level, and the pets picture. The tags dropdown shows the tags that the user can use. The personal information dropdown shows the users name and the users goal. The logout button takes them back to the login/sign up page. Since it is a fragment in the navagation, they can use the nav bar to navigate back to the home or analytics page

Statistics:
The statistics page keeps track of finished and incomplete or unstarted tasks that the user has created. The user can navigate to it using the navigation bar on the bottom of the screen. The three buttons at the top are different time deliniations for Day, Week, and Month. On click of each, it will show the start date that it is setting the scope of tasks to consider. Then the data section shows the list of tags that were used for each event. Lastly we have a pie chart that is makeup all the tasks in that scope. The list of events included are of incomplete and finished tasks combined. 

Pet System:
The pet system is the key feature of our app and what we propose which makes our app unique from other timers. The pet system is first saved when the user creates a new profile when they pick a profile picture and a name for the pet. On creation of a new account, the pet will have 0 points. When they run a timer or stopwatch, after 30 seconds the user will receive 10 points. This is updated for that users pet in firebase after the user backs out or the task completes. We use delimiters of 1000 points for each level. So if the user reaches 1000 points, which is after some use of a task, it will update firebase. Then when they return to the home page and/or profile, they will see their profile picture for their pet is updated to the pet at a higher level. The higher level is designated with some accessory like a hat.


