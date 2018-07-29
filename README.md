# PokeBus
You already keep Pokemon locked away in some crappy ball... So why not take it a step further and make their entire existance consist of taking you places and nothing else. They will literally only exist to take you to your distination, then *poof* - dead. It's a win-win!

I know, I know, it sounds too good to be true. But I assure you, it gets *even better!*

**TRIGGER WARNING**  
This plugin uses mixins to *safely* inject some code in to Pixelmon and Minecraft sources to allow the AI to function.
Rest assured, the amount of injected code is *very* miniscule and should in no way break anything.
Of course if you want to see for yourself, feel free to browse the source code.

## What does this plugin even do?
This plugin allows you to turn regular ol' statues in to PokeBus Stops that can be used by players. Interacting with a PokeBus Stop, assuming it is correctly set up, will spawn a PokeBus that will take you to a pre-defined set of destinations in an automatic or custom route.

### What is a PokeBus Stop?
A PokeBus Stop is a statue that holds information regarding the PokeBuses.

### What is a PokeBus?
A PokeBus is a Pokemon with the exact traits of it's parent statue (growth, shinyness, pokemon, etc.) but has been stripped of all it's Pokemon AIs and is essentially just another brainless mob waiting for orders.

### What is a PokeBus Driver?
Each PokeBus will have a chicken driving, because why not. Each chicken has undergone rutheless PokeBus training that has more than likely left deep-seeded psychological issues that may cause them to temporarily clock-out of life and start doing burnouts mid-ride. Don't worry, they *usually* come-to after a few seconds. If not just de-mount, it's not like he'll care.

If you don't like him you can set him free in the config. Although, his only skills are driving Pokemon so he most likely won't make it past the first night and it'll be on your conscience. Whatever lets you sleep at night.

## Features
* PokeBuses use the traits of the statue associated with it's parent PokeBus Stop dynamically, allowing any statue changes to immediately be applied to the PokeBuses without any need for pesky reloads.

* Give PokeBuses their own **custom** names! Each PokeBus Stop can have their own **seperate** list of names for their PokeBus Drivers to use. If no names are set for a PokeBus Stop, it will use default names givin in the config. **Supports colour codes**

* Neat destination system and AI that allows you to set multiple destinations for each PokeBus Stop and easily create a custom path for the PokeBus to follow using the *ghost destinations* system. **Wiki coming soon(tm)**

* Players can get off the PokeBus at any time by de-mounting. Whether it be at a destination or in the middle of nowhere. 

* PokeBuses are highly configurable and uses a **sexy** interactive chat system to do so. Better than porn confirmed.

## Commands
* `/pokebus stops` - pokebus.admin.stops
* `/pokebus reload` - pokebus.admin.reload

## Common Problems
* **Pokemon going in circles:** Lower the travel speed and make the growth smaller.  

* **Problem getting to destination error:** Most likely the destination as above the maximum pathing range distance. Increase in config.  
* **Cannot get to destination:** Use ghost destinations to make a custom path to follow. Pretty much a must-do for any complex or long distance destinations.  
