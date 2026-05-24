# Rocket Builder Academy

Rocket Builder Academy is a kid-friendly Java game about designing and testing rockets. Players can build model rockets or choose real-life orbital-style rockets such as Saturn V, Space Launch System, and Delta IV Heavy. The game grades each design on altitude, delta-v, reusability, safety, cost, and smart fuel use.

## Run It

Compile:

```sh
javac src/RocketDesignerGame.java
```

Run:

```sh
java -cp src RocketDesignerGame
```

## How To Play

1. Pick `Model rocket` or `Real-life orbital rocket`.
2. Choose a launch world such as Earth, the Moon, Mars, Venus, Mercury, or Titan.
3. Pick rocket parts in the Design Lab.
4. Choose `Light theme`, `Rocket Academy blue`, or `Dark mode`.
5. Check the engine Isp and estimated delta-v.
6. Choose `Predetermined staging` for real rocket separation events or `Custom staging` to pick the number of stages and each stage's flight role.
7. Press `Launch Simulation`.
8. Read the Mission Report.
9. Improve the design and launch again.

## Mission Tools

- `Delta-v` shows the rocket's estimated speed-changing budget.
- `Engine Isp` shows sea-level and vacuum specific impulse for each engine.
- `Launch world` changes gravity, air density, drag, scoring targets, and the background.
- `Theme` switches between the original light colors, a Rocket Academy blue palette, and dark mode.
- `Show atmosphere layers` switches from a lower-atmosphere launch view to a layer view with space and other planets.
- If a rocket reaches space, the animation changes to a space view with stars and the curved launch world below it.
- Model rockets can fly an egg passenger and deploy a parachute above the rocket.
- Orbital rockets can aim for the space line and a low-orbit delta-v target.
- Custom staging lets players choose takeoff booster, core, upper-atmosphere, and space/vacuum roles.
- Preset staging includes Saturn V, SLS Block 1, and Delta IV Heavy separation events.
- Parachutes only work when there is enough atmosphere. They are useless dead weight on airless worlds such as the Moon and Mercury.

The real rocket numbers are simplified for learning and gameplay, not for mission planning.

The goal is not just to fly high. A great rocket also has enough useful delta-v, lands gently, stays stable, protects payloads, and stays within budget.
