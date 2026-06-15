Spendly is a Android application designed to help users track their expenses, manage budgets, and visualize their spending habits.

## Key Features
- **User Authentication**: Secure login and registration.
- **Expense Tracking**: Add expenses with amount, date, description, and categories.
- **Budget Goals**: Set minimum and maximum monthly spending goals using interactive SeekBars.
- **Category Management**: Organize expenses into custom categories.
- **Visual Reports**: View spending breakdowns using bar charts.
- **Data Persistence**: Powered by RoomDB for offline-first capability.

## Technical Requirements Implemented
- **Layouts**: Comprehensive use of LinearLayout, GridLayout, and ScrollView.
- **UI Elements**: EditText (with validation), NumberFormat (currency formatting), SeekBar (interactive goal setting).
- **Event Handling**: Button clicks, SeekBar changes, and Date/Time pickers.
- **Architecture**: Multiple Activities connected via Intents, RoomDB for local storage.

### Required features
- **Category spending vs. goals chart** (`Reports` screen) — a bar chart showing how much was spent per category over the selected date range, with dashed reference lines for the user's overall **minimum** and **maximum** monthly budget goals so trends and over/under-spending are easy to spot at a glance.
- **Visual budget progress** (Dashboard / `Login` screen) — the monthly progress bar and total now change colour based on how the user is doing against their min/max goals: green when on track, amber when under the minimum goal, and red when the maximum goal has been exceeded (overspending is highlighted visually).
- **Gamification** — a new "🏆 Achievements" screen tracks a daily logging streak and awards badges for consistent logging and for staying within budget goals.
- New **app icon** based on the Spendly wallet logo (adaptive icon, all densities).

### Own feature 1 — Daily Streak & Achievement Badges
A dedicated **Achievements** screen (accessible from the dashboard) shows:
- A **streak counter** (🔥) — the number of consecutive days the user has logged at least one expense, also shown as a badge on the dashboard.
- A list of **badges** that unlock automatically based on the user's data: 🌱 First Steps, 🔥 3-Day Streak, 🔥🔥 7-Day Streak, 📝 Habit Builder (10 logging days), 🎯 Goal Setter, 💪 Budget Master (stayed within max goal last month), 💰 On Target (landed between min and max goal last month).

Badges are computed dynamically from existing `Expense` and `BudgetGoal` records — no extra database table is required.

### Own feature 2 — Goal-aware dashboard with min/max range
The dashboard now shows the user's **goal range** (min – max) underneath the monthly progress bar, and colour-codes the progress bar/total amount (green / amber / red) so users can immediately see whether they're under, within, or over their planned spending range.

https://youtu.be/q8q_obi6VsU?si=2Y71LGsmfbxOsj6E video for the app
