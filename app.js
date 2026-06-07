/**
 * GlowFit - Gym Workout Tracker ABC Push Pull Legs
 */

class GlowFitApp {
    constructor() {
                        this.defaultExercises = [
            { id: 'push_1', name: 'Supino reto com barra', category: 'Push', target: 'peito', defaultSets: 4, defaultReps: 10 },
            { id: 'push_2', name: 'Supino inclinado com halteres', category: 'Push', target: 'peito', defaultSets: 3, defaultReps: 12 },
            { id: 'push_3', name: 'Desenvolvimento de ombro sentado com barra', category: 'Push', target: 'ombros', defaultSets: 3, defaultReps: 10 },
            { id: 'push_4', name: 'Elevação lateral com halteres', category: 'Push', target: 'ombros', defaultSets: 4, defaultReps: 15 },
            { id: 'push_5', name: 'Tríceps na polia com barra V', category: 'Push', target: 'triceps', defaultSets: 4, defaultReps: 12 },
            { id: 'push_6', name: 'Supino reto com halteres', category: 'Push', target: 'peito', defaultSets: 4, defaultReps: 10 },
            { id: 'push_7', name: 'Crucifixo reto com halteres', category: 'Push', target: 'peito', defaultSets: 3, defaultReps: 12 },
            { id: 'push_8', name: 'Crossover na polia alta', category: 'Push', target: 'peito', defaultSets: 3, defaultReps: 12 },
            { id: 'push_9', name: 'Tríceps testa com halteres', category: 'Push', target: 'triceps', defaultSets: 3, defaultReps: 10 },
            { id: 'push_10', name: 'Tríceps testa com barra', category: 'Push', target: 'triceps', defaultSets: 3, defaultReps: 10 },
            { id: 'push_11', name: 'Tríceps coice na polia', category: 'Push', target: 'triceps', defaultSets: 3, defaultReps: 12 },
            { id: 'push_12', name: 'Desenvolvimento de ombro sentado com halteres', category: 'Push', target: 'ombros', defaultSets: 3, defaultReps: 10 },
            { id: 'push_13', name: 'Elevação frontal com halteres', category: 'Push', target: 'ombros', defaultSets: 3, defaultReps: 12 },
            { id: 'push_14', name: 'Flexão de braço', category: 'Push', target: 'peito', defaultSets: 3, defaultReps: 15 },
            
            { id: 'pull_1', name: 'Puxada alta na polia com amplitude máxima', category: 'Pull', target: 'costas', defaultSets: 4, defaultReps: 12 },
            { id: 'pull_2', name: 'Remada curvada com barra', category: 'Pull', target: 'costas', defaultSets: 4, defaultReps: 10 },
            { id: 'pull_3', name: 'Remada sentado na polia', category: 'Pull', target: 'costas', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_4', name: 'Rosca direta com barra', category: 'Pull', target: 'biceps', defaultSets: 3, defaultReps: 10 },
            { id: 'pull_5', name: 'Rosca martelo com halteres', category: 'Pull', target: 'biceps', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_6', name: 'Puxada alta pegada fechada na polia', category: 'Pull', target: 'costas', defaultSets: 4, defaultReps: 12 },
            { id: 'pull_7', name: 'Pull down na polia alta', category: 'Pull', target: 'costas', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_8', name: 'Levantamento terra com barra', category: 'Pull', target: 'costas', defaultSets: 3, defaultReps: 8 },
            { id: 'pull_9', name: 'Rosca bíceps alternada com halteres', category: 'Pull', target: 'biceps', defaultSets: 3, defaultReps: 10 },
            { id: 'pull_10', name: 'Rosca concentrada com halteres', category: 'Pull', target: 'biceps', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_11', name: 'Rosca inversa com barra', category: 'Pull', target: 'biceps', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_12', name: 'Crucifixo invertido com halteres', category: 'Pull', target: 'ombros', defaultSets: 3, defaultReps: 12 },
            { id: 'pull_13', name: 'Encolhimento de ombro com halteres', category: 'Pull', target: 'costas', defaultSets: 4, defaultReps: 15 },
            
            { id: 'legs_1', name: 'Agachamento livre com barra', category: 'Legs', target: 'quadriceps', defaultSets: 4, defaultReps: 8 },
            { id: 'legs_2', name: 'Leg press unilateral articulado', category: 'Legs', target: 'quadriceps', defaultSets: 4, defaultReps: 10 },
            { id: 'legs_3', name: 'Cadeira extensora articulada', category: 'Legs', target: 'quadriceps', defaultSets: 3, defaultReps: 12 },
            { id: 'legs_4', name: 'Mesa flexora deitada articulada', category: 'Legs', target: 'posteriores', defaultSets: 3, defaultReps: 12 },
            { id: 'legs_5', name: 'Panturrilha em pé com barra', category: 'Legs', target: 'panturrilha', defaultSets: 4, defaultReps: 15 },
            { id: 'legs_6', name: 'Afundo com halteres', category: 'Legs', target: 'quadriceps', defaultSets: 3, defaultReps: 10 },
            { id: 'legs_7', name: 'Agachamento búlgaro unilateral com elástico', category: 'Legs', target: 'quadriceps', defaultSets: 3, defaultReps: 10 },
            { id: 'legs_8', name: 'Stiff/levantamento terra romeno', category: 'Legs', target: 'posteriores', defaultSets: 3, defaultReps: 10 },
            { id: 'legs_9', name: 'Stiff com halteres', category: 'Legs', target: 'posteriores', defaultSets: 3, defaultReps: 10 },
            { id: 'legs_10', name: 'Elevação pélvica de joelhos com elástico', category: 'Legs', target: 'posteriores', defaultSets: 3, defaultReps: 10 },
            { id: 'legs_11', name: 'Cadeira adutora articulada', category: 'Legs', target: 'quadriceps', defaultSets: 3, defaultReps: 12 },
            { id: 'legs_12', name: 'Cadeira abdutora articulada', category: 'Legs', target: 'posteriores', defaultSets: 3, defaultReps: 12 },
            { id: 'legs_13', name: 'Panturrilha sentado com barra', category: 'Legs', target: 'panturrilha', defaultSets: 4, defaultReps: 15 }
        ];

        this.exercises = [];
        this.routines = {};
        this.lastCompletedWorkout = 'Legs';
        
                        this.defaultRoutines = {
            Push: ['Supino reto com barra', 'Supino inclinado com halteres', 'Desenvolvimento de ombro sentado com barra', 'Elevação lateral com halteres', 'Tríceps na polia com barra V'],
            Pull: ['Puxada alta na polia com amplitude máxima', 'Remada curvada com barra', 'Remada sentado na polia', 'Rosca direta com barra', 'Rosca martelo com halteres'],
            Legs: ['Agachamento livre com barra', 'Leg press unilateral articulado', 'Cadeira extensora articulada', 'Mesa flexora deitada articulada', 'Panturrilha em pé com barra']
        };
        
        this.activeRoutineTab = 'Push';
        this.exerciseGifMap = {};
        this.exerciseGifMapPt = {};
        this.loadGifMetadata();

        this.init();
    }

    async loadGifMetadata() {
        try {
            const response = await fetch('./gifs/mapping_metadata.json');
            if (response.ok) {
                const data = await response.json();
                this.exerciseGifMap = data;
                
                // Build Portuguese lookup map
                this.exerciseGifMapPt = {};
                Object.entries(data).forEach(([gifName, ex]) => {
                    if (ex.name_pt) {
                        this.exerciseGifMapPt[ex.name_pt.toLowerCase().trim()] = gifName;
                    }
                });

                // Merge all database exercises into this.exercises if they are not already there
                const existingNames = new Set(this.exercises.map(e => e.name.toLowerCase().trim()));
                let updated = false;
                
                Object.values(data).forEach(ex => {
                    const cleanName = ex.name_pt ? ex.name_pt.toLowerCase().trim() : ex.name.toLowerCase().trim();
                    if (!existingNames.has(cleanName)) {
                        // Map category
                        let category = 'Push';
                        const bp = ex.category ? ex.category.toLowerCase() : '';
                        const targetMuscle = ex.target ? ex.target.toLowerCase() : '';
                        
                        if (bp.includes('back') || bp.includes('cardio') || bp.includes('neck') || targetMuscle.includes('biceps') || targetMuscle.includes('forearm')) {
                            category = 'Pull';
                        } else if (bp.includes('leg') || bp.includes('calves') || bp.includes('waist') || targetMuscle.includes('abs') || targetMuscle.includes('calf') || targetMuscle.includes('glute')) {
                            category = 'Legs';
                        } else if (bp.includes('arms') && targetMuscle.includes('triceps')) {
                            category = 'Push';
                        } else if (bp.includes('arms') && (targetMuscle.includes('biceps') || targetMuscle.includes('forearm'))) {
                            category = 'Pull';
                        }
                        
                        // Map target muscle to Portuguese tag
                        let target = 'peito';
                        if (category === 'Pull') target = 'costas';
                        if (category === 'Legs') target = 'quadriceps';
                        
                        if (targetMuscle) {
                            if (targetMuscle.includes('peito') || targetMuscle.includes('pectoral')) target = 'peito';
                            else if (targetMuscle.includes('delts') || targetMuscle.includes('shoulder') || targetMuscle.includes('deltoid')) target = 'ombros';
                            else if (targetMuscle.includes('triceps')) target = 'triceps';
                            else if (targetMuscle.includes('biceps')) target = 'biceps';
                            else if (targetMuscle.includes('lats') || targetMuscle.includes('back') || targetMuscle.includes('spine') || targetMuscle.includes('traps') || targetMuscle.includes('trapezius')) target = 'costas';
                            else if (targetMuscle.includes('quads') || targetMuscle.includes('quadriceps') || targetMuscle.includes('thigh')) target = 'quadriceps';
                            else if (targetMuscle.includes('glute') || targetMuscle.includes('hamstring') || targetMuscle.includes('posterior')) target = 'posteriores';
                            else if (targetMuscle.includes('calf') || targetMuscle.includes('calves') || targetMuscle.includes('soleus') || targetMuscle.includes('gastrocnemius')) target = 'panturrilha';
                        }

                        this.exercises.push({
                            id: 'db_' + ex.id,
                            name: ex.name_pt || ex.name,
                            category: category,
                            target: target,
                            defaultSets: 3,
                            defaultReps: 12
                        });
                        updated = true;
                    }
                });
                
                if (updated) {
                    this.saveExercises();
                }

                // Populate the datalist for auto-complete search!
                const datalist = document.getElementById('exercise-suggestions');
                if (datalist) {
                    const uniqueNames = new Set(Object.values(data).map(ex => ex.name_pt || ex.name));
                    datalist.innerHTML = Array.from(uniqueNames)
                        .sort()
                        .map(name => `<option value="${name}"></option>`)
                        .join('');
                }

                // Re-render to show correct custom GIFs once metadata is loaded
                this.renderActiveRoutine();
                this.renderExercises();
            }
        } catch (err) {
            console.error('Error loading GIF metadata:', err);
        }
    }

    init() {
        this.loadLocalStorage();
        this.setupNavigation();
        this.renderExercises();
        
        // Auto select tab of the recommended workout of the day
        const suggested = this.getSuggestedWorkout();
        this.selectRoutineTab(suggested);
        
        this.updateDashboard();
    }

    loadLocalStorage() {
        const currentVersion = 5;
        const savedVersion = parseInt(localStorage.getItem('glowfit_db_version') || '0');

        // Load custom or default exercises
        const savedExercises = localStorage.getItem('glowfit_exercises');
        if (savedExercises && savedVersion === currentVersion) {
            this.exercises = JSON.parse(savedExercises);
        } else {
            localStorage.removeItem('glowfit_exercises');
            localStorage.removeItem('glowfit_routines');
            localStorage.setItem('glowfit_db_version', currentVersion.toString());
            this.exercises = [...this.defaultExercises];
            localStorage.setItem('glowfit_exercises', JSON.stringify(this.exercises));
        }

        // Load active routines
        const savedRoutines = localStorage.getItem('glowfit_routines');
        if (savedRoutines && savedVersion === currentVersion) {
            this.routines = JSON.parse(savedRoutines);
        } else {
            this.routines = { ...this.defaultRoutines };
            localStorage.setItem('glowfit_routines', JSON.stringify(this.routines));
        }

                // Load last completed suggested workout to roll the sequence
        const savedLast = localStorage.getItem('glowfit_last_completed');
        if (savedLast) {
            this.lastCompletedWorkout = savedLast;
        } else {
            this.lastCompletedWorkout = 'Legs';
        }
    }

    saveExercises() {
        localStorage.setItem('glowfit_exercises', JSON.stringify(this.exercises));
    }

    saveRoutines() {
        localStorage.setItem('glowfit_routines', JSON.stringify(this.routines));
    }

    setupNavigation() {
        const navItems = document.querySelectorAll('.nav-item, .mobile-nav-item');
        navItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const targetTab = item.getAttribute('data-tab');
                this.switchTab(targetTab);
            });
        });
    }

    switchTab(tabId) {
        // Hide all tabs
        document.querySelectorAll('.tab-content').forEach(tab => {
            tab.classList.remove('active');
        });
        // Show target tab
        const activeTab = document.getElementById(`tab-${tabId}`);
        if (activeTab) {
            activeTab.classList.add('active');
        }

        // Update nav styling
        document.querySelectorAll('.nav-item, .mobile-nav-item').forEach(item => {
            if (item.getAttribute('data-tab') === tabId) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });
    }

    updateDashboard() {
        // Update Daily Recommendation Card
        const suggested = this.getSuggestedWorkout();
        let displaySuggested = suggested;
        if (suggested === 'Push') displaySuggested = 'A - Push (Empurrar)';
        if (suggested === 'Pull') displaySuggested = 'B - Pull (Puxar)';
        if (suggested === 'Legs') displaySuggested = 'C - Legs (Pernas)';
        document.getElementById('daily-suggestion-text').innerText = `Recomendação: Treino ${displaySuggested}`;
        document.getElementById('dashboard-muscle-map-container').innerHTML = this.getMuscleMapSVG(suggested);

        this.renderWeeklyPlanner();
    }

    getSuggestedWorkout() {
        const last = this.lastCompletedWorkout || 'Legs';
        if (last === 'Push') return 'Pull';
        if (last === 'Pull') return 'Legs';
        return 'Push';
    }

    focusSuggestedWorkout() {
        const suggested = this.getSuggestedWorkout();
        this.selectRoutineTab(suggested);
        document.getElementById('workout-section-title').scrollIntoView({ behavior: 'smooth' });
    }

    markSuggestedWorkoutDone() {
        const suggested = this.getSuggestedWorkout();
        this.lastCompletedWorkout = suggested;
        localStorage.setItem('glowfit_last_completed', suggested);
        
        // Rerender dashboard recommendation and select the new suggested tab
        this.updateDashboard();
        const newSuggested = this.getSuggestedWorkout();
        this.selectRoutineTab(newSuggested);
        
        alert(`Treino ${suggested} concluído! Parabéns pelo treino. 🔥`);
    }

    renderWeeklyPlanner() {
        const container = document.getElementById('planner-days-container');
        if (!container) return;

        const dayNames = ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta'];
        
        // Get start of the current week (Monday)
        const today = new Date();
        const currentDay = today.getDay(); // 0 is Sun, 1 is Mon, etc.
        const mondayOffset = currentDay === 0 ? -6 : 1 - currentDay;
        const mondayDate = new Date(today);
        mondayDate.setDate(today.getDate() + mondayOffset);
        mondayDate.setHours(0, 0, 0, 0);

        let lastSplit = this.lastCompletedWorkout || 'Legs';

        let html = '';
        
        for (let i = 0; i < 5; i++) {
            const dayDate = new Date(mondayDate);
            dayDate.setDate(mondayDate.getDate() + i);
            const isToday = dayDate.toDateString() === today.toDateString();
            
            let dayClass = 'upcoming';
            let statusText = 'Planejado';

            // Future/Current days sequence rolável projection
            if (lastSplit === 'Push') lastSplit = 'Pull';
            else if (lastSplit === 'Pull') lastSplit = 'Legs';
            else lastSplit = 'Push';

            if (isToday) {
                dayClass = 'next';
                statusText = 'Hoje ⚡';
            }

            let displayName = lastSplit;
            if (lastSplit === 'Push') displayName = 'Push (A)';
            else if (lastSplit === 'Pull') displayName = 'Pull (B)';
            else if (lastSplit === 'Legs') displayName = 'Legs (C)';

            html += `
                <div class="planner-day ${dayClass}">
                    <span class="planner-day-name">${dayNames[i]}</span>
                    <span class="planner-day-workout">${displayName}</span>
                    <span class="planner-day-status">${statusText}</span>
                </div>
            `;
        }

        container.innerHTML = html;
    }

    selectRoutineTab(category) {
        this.activeRoutineTab = category;
        
        // Style selected buttons
        document.querySelectorAll('.btn-routine-tab').forEach(btn => {
            if (btn.id === `tab-btn-${category}`) {
                btn.classList.remove('btn-secondary');
                btn.classList.add('btn-primary');
                btn.classList.add('active');
            } else {
                btn.classList.remove('btn-primary');
                btn.classList.remove('active');
                btn.classList.add('btn-secondary');
            }
        });
        
        this.renderActiveRoutine();
    }

    renderActiveRoutine() {
        const container = document.getElementById('routine-exercises-list');
        if (!container) return;
        
        container.innerHTML = '';
        
        const activeList = this.routines[this.activeRoutineTab] || [];
        
        activeList.forEach((name, index) => {
            const card = document.createElement('div');
            card.className = 'card exercise-log-card';
            
            const template = this.exercises.find(e => e.name.toLowerCase().trim() === name.toLowerCase().trim());
            let badgeHtml = '';
            if (template && template.target) {
                const targetLabel = template.target.charAt(0).toUpperCase() + template.target.slice(1);
                badgeHtml = `<span class="badge-muscle badge-${template.target.toLowerCase()}" style="margin-left: 0.5rem;">${targetLabel}</span>`;
            }
            
            card.innerHTML = `
                <div class="exercise-header">
                    <h3 class="exercise-title" style="display: flex; align-items: center; flex-wrap: wrap;">
                        <span>${name}</span>
                        ${badgeHtml}
                    </h3>
                    <div style="display: flex; gap: 0.5rem; align-items: center;">
                        <button class="btn btn-secondary btn-demo" onclick="app.showExerciseTips('${name}')" style="margin-left: 0;">Dicas</button>
                        <button class="btn btn-secondary" onclick="app.openSubstituteExerciseModal(${index})" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">Substituir</button>
                        <button class="btn btn-danger" onclick="app.removeExerciseFromRoutine(${index})" style="padding: 0.25rem 0.5rem; font-size: 0.8rem; background: transparent; border: none; font-size: 0.95rem; margin-left: 0.25rem;">Remover</button>
                    </div>
                </div>
                <div class="exercise-card-body">
                    <div class="exercise-gif-wrapper">
                        ${this.getExerciseGifHtml(name)}
                    </div>
                    <div class="exercise-table-wrapper" style="display: flex; flex-direction: column; justify-content: center; height: 100px;">
                        <div style="font-size: 1.15rem; font-weight: 700; color: var(--color-accent); margin-bottom: 0.25rem;">3 séries</div>
                        <div style="color: var(--text-secondary); font-size: 0.95rem;">6 a 12 repetições</div>
                    </div>
                </div>
            `;
            container.appendChild(card);
        });
        
        // Update muscle map target highlight for this routine
        const mapContainer = document.getElementById('routine-muscle-map-container');
        if (mapContainer) {
            mapContainer.innerHTML = this.getMuscleMapSVG(this.activeRoutineTab);
        }
    }

    filterExercises() {
        const query = document.getElementById('search-exercise-db').value.toLowerCase().trim();
        const categoryFilter = document.getElementById('filter-exercise-category').value;
        const targetFilter = document.getElementById('filter-exercise-target').value;
        this.renderExercises(query, 0, categoryFilter, targetFilter);
    }

    renderExercises(query = '', customLimit = 0, categoryFilter = '', targetFilter = '') {
        const container = document.getElementById('exercises-container');
        if (!container) return;

        container.innerHTML = '';
        
        // Update database total count badge
        const totalCountBadge = document.getElementById('db-total-count');
        if (totalCountBadge) {
            totalCountBadge.innerText = `${this.exercises.length} movimentos`;
        }

        let list = this.exercises;
        
        // Apply filters
        if (query) {
            list = list.filter(ex => ex.name.toLowerCase().includes(query));
        }
        if (categoryFilter) {
            list = list.filter(ex => ex.category === categoryFilter);
        }
        if (targetFilter) {
            list = list.filter(ex => {
                if (targetFilter === 'geral') {
                    return !ex.target || ['peito', 'costas', 'biceps', 'triceps', 'ombros', 'quadriceps', 'posteriores', 'panturrilha'].indexOf(ex.target.toLowerCase()) === -1;
                }
                return ex.target && ex.target.toLowerCase() === targetFilter.toLowerCase();
            });
        }

        // Limit rendering size to prevent DOM lag, search reveals all matched
        const limit = customLimit || (query || categoryFilter || targetFilter ? list.length : 60);
        const listToRender = list.slice(0, limit);

        listToRender.forEach(ex => {
            const card = document.createElement('div');
            card.className = 'card exercise-item-card';

            let badgeClass = ex.target ? ex.target.toLowerCase() : 'peito';
            let targetLabel = ex.target ? ex.target.charAt(0).toUpperCase() + ex.target.slice(1) : 'Geral';

            card.innerHTML = `
                <div class="exercise-card-body" style="display: flex; gap: 1rem; width: 100%; align-items: center;">
                    <div class="exercise-gif-wrapper" style="flex-shrink: 0;">
                        ${this.getExerciseGifHtml(ex.name)}
                    </div>
                    <div class="exercise-info-block" style="flex-grow: 1;">
                        <h3 style="display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap;">
                            <span>${ex.name}</span>
                            <span class="badge-muscle badge-${badgeClass}">${targetLabel}</span>
                        </h3>
                        <p style="margin-top: 0.5rem;">Categoria: <strong>${ex.category}</strong> | Padrão: 3x 6-12 reps</p>
                        <div style="display: flex; align-items: center; gap: 0.5rem; margin-top: 0.75rem;">
                            <button class="btn btn-secondary btn-demo" onclick="app.showExerciseTips('${ex.name.replace(/'/g, "\\'")}')" style="margin-left: 0; padding: 0.25rem 0.5rem; font-size: 0.75rem;">Dicas</button>
                            <button class="btn btn-danger" onclick="app.deleteExercise('${ex.id}')" style="padding: 0.25rem 0.5rem; background: transparent; border: none; font-size: 0.9rem;">Remover</button>
                        </div>
                    </div>
                </div>
            `;
            container.appendChild(card);
        });

        if (list.length > limit) {
            const loadMore = document.createElement('div');
            loadMore.style.textAlign = 'center';
            loadMore.style.width = '100%';
            loadMore.style.padding = '1.5rem';
            loadMore.innerHTML = `
                <div style="margin-bottom: 0.75rem; color: var(--text-secondary); font-size: 0.9rem;">
                    Mostrando ${limit} de ${list.length} exercícios filtrados.
                </div>
                <button class="btn btn-secondary" onclick="app.renderExercises('${query.replace(/'/g, "\\'")}', ${limit + 60}, '${categoryFilter}', '${targetFilter}')" style="margin: 0 auto; display: inline-block;">Carregar Mais</button>
            `;
            container.appendChild(loadMore);
        } else if (list.length === 0) {
            container.innerHTML = `
                <div style="text-align: center; width: 100%; padding: 3rem; color: var(--text-secondary);">
                    Nenhum exercício encontrado com os filtros selecionados.
                </div>
            `;
        }
    }

    openNewExerciseModal() {
        document.getElementById('new-exercise-modal').classList.add('active');
    }

    closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
    }

    saveNewExercise() {
        const nameInput = document.getElementById('new-exercise-name');
        const splitSelect = document.getElementById('new-exercise-split');

        const name = nameInput.value.trim();
        const category = splitSelect.value;

        if (!name) {
            alert('Por favor, informe o nome do exercício.');
            return;
        }

        // Try to find target muscle from mapping_metadata.json
        let target = 'peito';
        if (category === 'Pull') target = 'costas';
        if (category === 'Legs') target = 'quadriceps';

        const key = name.toLowerCase().trim();
        const clean = (str) => {
            return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "")
                .toLowerCase()
                .replace(/[\(\)\°\s\-/\'\,\.\&\+\#\!]+/g, '_')
                .replace(/^_+|_+$/g, '');
        };
        const cleanKey = clean(key);
        const gifName = cleanKey + '.gif';
        
        if (this.exerciseGifMap) {
            let metadata = this.exerciseGifMap[gifName];
            if (!metadata) {
                const matchedFilename = Object.keys(this.exerciseGifMap).find(fn => fn.replace('.gif', '') === cleanKey);
                if (matchedFilename) metadata = this.exerciseGifMap[matchedFilename];
            }
            if (metadata && metadata.target) {
                const t = metadata.target.toLowerCase();
                if (t.includes('peito') || t.includes('pectoral')) target = 'peito';
                else if (t.includes('delts') || t.includes('shoulder') || t.includes('deltoid')) target = 'ombros';
                else if (t.includes('triceps')) target = 'triceps';
                else if (t.includes('biceps')) target = 'biceps';
                else if (t.includes('lats') || t.includes('back') || t.includes('spine') || t.includes('traps') || t.includes('trapezius')) target = 'costas';
                else if (t.includes('quads') || t.includes('quadriceps') || t.includes('thigh')) target = 'quadriceps';
                else if (t.includes('glute') || t.includes('hamstring') || t.includes('posterior')) target = 'posteriores';
                else if (t.includes('calf') || t.includes('calves') || t.includes('soleus') || t.includes('gastrocnemius')) target = 'panturrilha';
            }
        }

        const newEx = {
            id: 'custom_' + Date.now(),
            name: name,
            category: category,
            target: target,
            defaultSets: 3,
            defaultReps: 12
        };

        this.exercises.push(newEx);
        this.saveExercises();
        this.renderExercises();
        
        // Reset and close
        nameInput.value = '';
        this.closeModal('new-exercise-modal');
    }

    deleteExercise(id) {
        if (confirm('Deseja realmente excluir este exercício do banco de dados?')) {
            this.exercises = this.exercises.filter(ex => ex.id !== id);
            this.saveExercises();
            this.renderExercises();
            
            // Re-render routines in case the exercise was in one of them
            this.renderActiveRoutine();
        }
    }

    openSubstituteExerciseModal(index) {
        this.substitutingRoutineIndex = index;
        const currentList = this.routines[this.activeRoutineTab] || [];
        const nameToReplace = currentList[index];
        const template = this.exercises.find(e => e.name.toLowerCase().trim() === nameToReplace.toLowerCase().trim());
        const categoryToUse = template ? template.category : this.activeRoutineTab;
        
        document.getElementById('substitute-origin-name').innerText = nameToReplace;
        
        const select = document.getElementById('select-exercise-to-substitute');
        select.innerHTML = '';
        
        // Filter exercises in the same category that are not already in the routine
        const sameCategoryExs = this.exercises.filter(ex => 
            ex.category === categoryToUse && !currentList.includes(ex.name)
        );

        if (sameCategoryExs.length === 0) {
            select.innerHTML = `<option value="">Nenhum exercício alternativo disponível</option>`;
        } else {
            const targetMuscle = template?.target;
            sameCategoryExs.sort((a, b) => {
                if (a.target === targetMuscle && b.target !== targetMuscle) return -1;
                if (a.target !== targetMuscle && b.target === targetMuscle) return 1;
                return 0;
            });

            sameCategoryExs.forEach(ex => {
                const isSameMuscle = ex.target === targetMuscle;
                const label = isSameMuscle 
                    ? `[Mesmo Foco - ${ex.target || 'Geral'}] ${ex.name}` 
                    : `[Alternativa] ${ex.name}`;
                select.innerHTML += `<option value="${ex.name}">${label}</option>`;
            });
        }

        document.getElementById('substitute-exercise-modal').classList.add('active');
    }

    executeExerciseSubstitution() {
        if (this.substitutingRoutineIndex === undefined) return;
        const select = document.getElementById('select-exercise-to-substitute');
        const newExerciseName = select.value;
        if (!newExerciseName) {
            this.closeModal('substitute-exercise-modal');
            return;
        }

        // Update the routine list
        this.routines[this.activeRoutineTab][this.substitutingRoutineIndex] = newExerciseName;
        this.saveRoutines();

        this.renderActiveRoutine();
        this.closeModal('substitute-exercise-modal');
    }

    openAddExerciseToRoutineModal() {
        const select = document.getElementById('select-exercise-to-add');
        if (!select) return;
        
        select.innerHTML = '';
        
        const currentList = this.routines[this.activeRoutineTab] || [];
        
        // Filter exercises in the database/custom lists that are in the same category and not already added
        const available = this.exercises.filter(ex => 
            ex.category === this.activeRoutineTab && !currentList.includes(ex.name)
        );

        if (available.length === 0) {
            select.innerHTML = `<option value="">Nenhum exercício disponível para adicionar</option>`;
        } else {
            available.sort((a, b) => a.name.localeCompare(b.name));
            available.forEach(ex => {
                select.innerHTML += `<option value="${ex.name}">${ex.name} (${ex.target || 'Geral'})</option>`;
            });
        }

        document.getElementById('add-exercise-to-workout-modal').classList.add('active');
    }

    addSelectedExerciseToSession() {
        const select = document.getElementById('select-exercise-to-add');
        if (!select) return;
        
        const name = select.value;
        if (!name) {
            this.closeModal('add-exercise-to-workout-modal');
            return;
        }

        if (!this.routines[this.activeRoutineTab]) {
            this.routines[this.activeRoutineTab] = [];
        }

        this.routines[this.activeRoutineTab].push(name);
        this.saveRoutines();
        this.renderActiveRoutine();
        
        this.closeModal('add-exercise-to-workout-modal');
    }

    removeExerciseFromRoutine(index) {
        if (confirm('Deseja realmente remover este exercício do treino atual?')) {
            this.routines[this.activeRoutineTab].splice(index, 1);
            this.saveRoutines();
            this.renderActiveRoutine();
        }
    }

    showExerciseTips(exerciseName) {
        const modal = document.getElementById('exercise-demo-modal');
        const title = document.getElementById('demo-modal-title');
        const animBox = document.getElementById('demo-modal-animation-box');
        const details = document.getElementById('demo-modal-details');

        if (!modal || !title || !animBox || !details) return;

        title.innerText = exerciseName;
        
        const key = exerciseName.toLowerCase().trim();
        let gifName = '';
        let metadata = null;
        
        if (this.exerciseGifMapPt && this.exerciseGifMapPt[key]) {
            gifName = this.exerciseGifMapPt[key];
            if (this.exerciseGifMap && this.exerciseGifMap[gifName]) {
                metadata = this.exerciseGifMap[gifName];
            }
        }

        if (!metadata && this.exerciseGifMap) {
            // Try clean lookup
            const clean = (str) => {
                return str.normalize("NFD").replace(/[̀-ͯ]/g, "")
                    .toLowerCase()
                    .replace(/[\(\)\°\s\-/'\,\.\&\+\#\!]+/g, '_')
                    .replace(/^_+|_+$/g, '');
            };
            const cleanKey = clean(key);
            const directGifName = cleanKey + '.gif';
            
            if (this.exerciseGifMap[directGifName]) {
                metadata = this.exerciseGifMap[directGifName];
                gifName = directGifName;
            } else {
                const matchedFilename = Object.keys(this.exerciseGifMap).find(fn => {
                    const cleanFn = fn.replace('.gif', '');
                    return cleanKey.includes(cleanFn) || cleanFn.includes(cleanKey);
                });
                if (matchedFilename) {
                    metadata = this.exerciseGifMap[matchedFilename];
                    gifName = matchedFilename;
                }
            }
        }

        if (gifName) {
            animBox.style.display = 'flex';
            animBox.innerHTML = `<img src="./gifs/${gifName}" style="max-height: 100%; max-width: 100%; object-fit: contain; border-radius: var(--radius-md);">`;
        } else {
            animBox.style.display = 'none';
        }

        const tips = this.getExerciseTips(exerciseName);
        let detailsHtml = '';

        if (metadata) {
            detailsHtml += `
                <div style="margin-bottom: 1.25rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.75rem;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary); margin-bottom: 0.4rem; text-align: left;">Foco & Equipamento:</div>
                    <div style="display: flex; flex-wrap: wrap; gap: 0.5rem;">
                        ${metadata.target ? `<span class="badge-muscle badge-${metadata.target.toLowerCase()}" style="font-size: 0.75rem; padding: 0.25rem 0.5rem; text-transform: capitalize; border-radius: 4px;">Target: ${metadata.target}</span>` : ''}
                        ${metadata.equipment ? `<span class="badge-muscle badge-geral" style="font-size: 0.75rem; padding: 0.25rem 0.5rem; background: rgba(30, 41, 59, 0.4); color: var(--text-primary); border: 1px solid var(--border-color); text-transform: capitalize; border-radius: 4px;"> ${metadata.equipment}</span>` : ''}
                        ${metadata.category ? `<span class="badge-muscle badge-geral" style="font-size: 0.75rem; padding: 0.25rem 0.5rem; background: rgba(30, 41, 59, 0.4); color: var(--text-primary); border: 1px solid var(--border-color); text-transform: capitalize; border-radius: 4px;"> ${metadata.category}</span>` : ''}
                    </div>
                </div>
            `;
        }

        detailsHtml += `
            <h4 style="margin-bottom: 0.5rem; font-weight: 600; text-align: left;">Dicas de Execução:</h4>
            <ul style="padding-left: 1.25rem; font-size: 0.9rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 0.4rem; text-align: left; margin-bottom: 1.25rem;">
                ${tips.map(tip => `<li>${tip}</li>`).join('')}
            </ul>
        `;

        if (metadata && metadata.instruction_steps_en && metadata.instruction_steps_en.length > 0) {
            detailsHtml += `
                <div style="border-top: 1px solid var(--border-color); padding-top: 0.75rem; margin-top: 0.75rem;">
                    <h4 style="margin-bottom: 0.5rem; font-weight: 600; text-align: left;">Passo a Passo (Inglês):</h4>
                    <ol style="padding-left: 1.25rem; font-size: 0.85rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 0.4rem; text-align: left; margin-bottom: 1rem;">
                        ${metadata.instruction_steps_en.map(step => `<li>${step}</li>`).join('')}
                    </ol>
                </div>
            `;
        } else if (metadata && metadata.instructions) {
            detailsHtml += `
                <div style="border-top: 1px solid var(--border-color); padding-top: 0.75rem; margin-top: 0.75rem;">
                    <h4 style="margin-bottom: 0.5rem; font-weight: 600; text-align: left;">Passo a Passo (Inglês):</h4>
                    <p style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.45; text-align: left; margin-bottom: 1rem;">${metadata.instructions}</p>
                </div>
            `;
        }

        details.innerHTML = detailsHtml;
        modal.classList.add('active');
    }

    getExerciseTips(name) {
        const lower = name.toLowerCase();
        if (lower.includes('supino') || lower.includes('bench press') || lower.includes('chest press') || lower.includes('fly') || lower.includes('push-up') || lower.includes('push up') || lower.includes('crossover') || lower.includes('cross-over')) {
            return [
                "Deite-se no banco mantendo os pés firmes no chão.",
                "Desça o peso devagar até a linha do peito.",
                "Empurre a barra/halteres estendendo os braços verticalmente.",
                "Mantenha as escápulas retraídas (ombros colados atrás)."
            ];
        }
        if (lower.includes('desenvolvimento') || lower.includes('overhead press') || lower.includes('shoulder press')) {
            return [
                "Mantenha o abdômen contraído para proteger a coluna.",
                "Suba o peso estendendo os braços verticalmente sem travar os cotovelos.",
                "Desça o peso controladamente até a altura do queixo."
            ];
        }
        if (lower.includes('elevação lateral') || lower.includes('elevacao lateral') || lower.includes('lateral raise') || lower.includes('rear delt') || lower.includes('elevação frontal') || lower.includes('elevacao frontal') || lower.includes('front raise')) {
            return [
                "Fique de pé com os joelhos levemente flexionados.",
                "Suba os braços para as laterais até a linha do ombro.",
                "Evite projetar os braços muito para trás ou balançar o corpo."
            ];
        }
        if (lower.includes('tríceps') || lower.includes('triceps') || lower.includes('kickback') || lower.includes('testa') || lower.includes('coice')) {
            return [
                "Mantenha os cotovelos colados ao tronco e totalmente imóveis.",
                "Estenda completamente o antebraço contraindo o tríceps.",
                "Retorne à posição inicial controlando o peso."
            ];
        }
        if (lower.includes('puxada') || lower.includes('pulldown') || lower.includes('pull down') || lower.includes('lat pull')) {
            return [
                "Puxe a barra em direção ao peitoral, inclinando o tronco levemente para trás.",
                "Concentre a força nos cotovelos e contraia as costas.",
                "Suba a barra controlando o peso até estender as costas."
            ];
        }
        if (lower.includes('remada') || lower.includes('row') || lower.includes('deadlift') || lower.includes('levantamento terra') || lower.includes('encolhimento') || lower.includes('shrug')) {
            return [
                "Mantenha o peitoral aberto e a coluna totalmente reta.",
                "Puxe em direção ao abdômen apertando as costas atrás.",
                "Retorne controlando o movimento na descida."
            ];
        }
        if (lower.includes('rosca') || lower.includes('curl') || lower.includes('martelo') || lower.includes('hammer')) {
            return [
                "Mantenha os cotovelos fixos ao lado do corpo.",
                "Suba o peso flexionando os antebraços em direção aos ombros.",
                "Evite balançar os ombros ou usar impulso do quadril."
            ];
        }
        if (lower.includes('agachamento') || lower.includes('squat') || lower.includes('leg press') || lower.includes('afundo') || lower.includes('lunge') || lower.includes('búlgaro') || lower.includes('bulgaro') || lower.includes('stiff') || lower.includes('hip thrust') || lower.includes('adductor') || lower.includes('abductor')) {
            return [
                "Mantenha os pés afastados na largura dos ombros.",
                "Desça projetando o quadril para trás como se fosse sentar.",
                "Mantenha o peito aberto e joelhos alinhados com as pontas dos pés."
            ];
        }
        if (lower.includes('panturrilha') || lower.includes('calf') || lower.includes('calves')) {
            return [
                "Suba na ponta dos pés o máximo possível contraindo a panturrilha.",
                "Desça alongando o músculo abaixo do nível de suporte.",
                "Realize o movimento de forma lenta e controlada."
            ];
        }
        return [
            "Mantenha a postura e a coluna alinhada.",
            "Realize o movimento de forma lenta e controlada.",
            "Concentre a mente na contração do músculo-alvo."
        ];
    }

    getExerciseGifHtml(name, className = "exercise-gif-inline") {
        const key = name.toLowerCase().trim();
        let path = '';

        // 1. Try resolving through Portuguese lookup map first
        if (this.exerciseGifMapPt && this.exerciseGifMapPt[key]) {
            path = `./gifs/${this.exerciseGifMapPt[key]}`;
        }

        // 2. Definitive 1-to-1 Mapping dictionary for all 40 exercises (fallback)
        if (!path) {
            const mappings = {
                // Push
                'supino reto com barra': './gifs/barbell_bench_press.gif',
                'supino inclinado com halteres': './gifs/dumbbell_incline_bench_press.gif',
                'desenvolvimento de ombro sentado com barra': './gifs/barbell_seated_overhead_press.gif',
                'elevação lateral com halteres': './gifs/dumbbell_lateral_raise.gif',
                'trícipe na polia com barra v': './gifs/cable_triceps_pushdown_v_bar.gif',
                'tríceps na polia com barra v': './gifs/cable_triceps_pushdown_v_bar.gif',
                'supino reto com halteres': './gifs/dumbbell_bench_press.gif',
                'crucifixo reto com halteres': './gifs/dumbbell_fly.gif',
                'crossover na polia alta': './gifs/cable_cross_over_lateral_pulldown.gif',
                'tríceps testa com halteres': './gifs/dumbbell_lying_triceps_extension.gif',
                'tríceps testa com barra': './gifs/barbell_lying_triceps_extension.gif',
                'tríceps coice na polia': './gifs/cable_kickback.gif',
                'desenvolvimento de ombro sentado com halteres': './gifs/dumbbell_seated_shoulder_press.gif',
                'elevação frontal com halteres': './gifs/dumbbell_front_raise.gif',
                'flexão de braço': './gifs/push_up.gif',

                // Pull
                'puxada alta na polia com amplitude máxima': './gifs/cable_lat_pulldown_full_range_of_motion.gif',
                'remada curvada com barra': './gifs/barbell_bent_over_row.gif',
                'remada sentado na polia': './gifs/cable_seated_row.gif',
                'rosca direta com barra': './gifs/barbell_curl.gif',
                'rosca martelo com halteres': './gifs/dumbbell_hammer_curl.gif',
                'puxada alta pegada fechada na polia': './gifs/cable_pulldown.gif',
                'pull down na polia alta': './gifs/cable_straight_arm_pulldown.gif',
                'levantamento terra com barra': './gifs/barbell_deadlift.gif',
                'rosca bíceps alternada com halteres': './gifs/dumbbell_alternate_biceps_curl.gif',
                'rosca concentrada com halteres': './gifs/dumbbell_concentration_curl.gif',
                'rosca inversa com barra': './gifs/barbell_reverse_curl.gif',
                'crucifixo invertido com halteres': './gifs/dumbbell_rear_delt_raise.gif',
                'encolhimento de ombro com halteres': './gifs/dumbbell_shrug.gif',

                // Legs
                'agachamento livre com barra': './gifs/barbell_full_squat.gif',
                'leg press unilateral articulado': './gifs/lever_alternate_leg_press.gif',
                'cadeira extensora articulada': './gifs/lever_leg_extension.gif',
                'mesa flexora deitada articulada': './gifs/lever_lying_leg_curl.gif',
                'panturrilha em pé com barra': './gifs/barbell_standing_calf_raise.gif',
                'afundo com halteres': './gifs/dumbbell_lunge.gif',
                'agachamento búlgaro unilateral com elástico': './gifs/band_one_arm_single_leg_split_squat.gif',
                'stiff/levantamento terra romeno': './gifs/barbell_romanian_deadlift.gif',
                'stiff com halteres': './gifs/dumbbell_stiff_leg_deadlift.gif',
                'elevação pélvica de joelhos com elástico': './gifs/resistance_band_hip_thrusts_on_knees_female.gif',
                'cadeira adutora articulada': './gifs/lever_seated_hip_adduction.gif',
                'cadeira abdutora articulada': './gifs/lever_seated_hip_abduction.gif',
                'panturrilha sentado com barra': './gifs/barbell_seated_calf_raise.gif'
            };
            path = mappings[key] || '';
        }

        // 3. Fallback to clean name matches
        if (!path && this.exerciseGifMap) {
            const clean = (str) => {
                return str.normalize("NFD").replace(/[̀-ͯ]/g, "")
                    .toLowerCase()
                    .replace(/[\(\)\°\s\-/'\,\.\&\+\#\!]+/g, '_')
                    .replace(/^_+|_+$/g, '');
            };

            const cleanKey = clean(key);
            const gifName = cleanKey + '.gif';

            if (this.exerciseGifMap[gifName]) {
                path = `./gifs/${gifName}`;
            } else {
                const matchedFilename = Object.keys(this.exerciseGifMap).find(fn => {
                    const cleanFn = fn.replace('.gif', '');
                    return cleanKey.includes(cleanFn) || cleanFn.includes(cleanKey);
                });
                if (matchedFilename) {
                    path = `./gifs/${matchedFilename}`;
                }
            }
        }

        // 4. Fallback keyword checks
        if (!path) {
            if (key.includes('supino')) path = './gifs/barbell_bench_press.gif';
            else if (key.includes('desenvolvimento')) path = './gifs/dumbbell_seated_shoulder_press.gif';
            else if (key.includes('elevação lateral') || key.includes('elevacao lateral')) path = './gifs/dumbbell_lateral_raise.gif';
            else if (key.includes('tríceps polia') || key.includes('triceps polia')) path = './gifs/cable_triceps_pushdown_v_bar.gif';
            else if (key.includes('tríceps coice') || key.includes('triceps coice')) path = './gifs/cable_kickback.gif';
            else if (key.includes('puxada alta')) path = './gifs/cable_lat_pulldown_full_range_of_motion.gif';
            else if (key.includes('remada')) path = './gifs/barbell_bent_over_row.gif';
            else if (key.includes('rosca')) path = './gifs/barbell_curl.gif';
            else if (key.includes('agachamento')) path = './gifs/barbell_full_squat.gif';
            else if (key.includes('leg press')) path = './gifs/lever_alternate_leg_press.gif';
            else if (key.includes('panturrilha')) path = './gifs/barbell_standing_calf_raise.gif';
        }

        if (path) {
            return `<img src="${path}" class="${className}" alt="${name}">`;
        }

        return `
        <div class="${className} no-gif-placeholder" style="display: flex; flex-direction: column; justify-content: center; align-items: center; font-size: 0.7rem; text-align: center; color: var(--text-muted); padding: 0.5rem; background: rgba(30, 41, 59, 0.4); border-radius: var(--radius-sm); height: 100px; width: 100px; border: 1px dashed var(--border-color); box-sizing: border-box;">
            <span>Sem GIF</span>
        </div>
        `;
    }

    getMuscleMapSVG(activeSplit) {
        const isPush = activeSplit === 'Push';
        const isPull = activeSplit === 'Pull';
        const isLegs = activeSplit === 'Legs';

        return `
        <svg class="muscle-svg" viewBox="0 0 120 220" xmlns="http://www.w3.org/2000/svg">
            <!-- Head -->
            <circle cx="60" cy="20" r="10" fill="#1e293b" stroke="#334155" stroke-width="1.5"/>
            <!-- Neck -->
            <rect x="57" y="30" width="6" height="6" fill="#1e293b" stroke="#334155" stroke-width="1.5"/>
            
            <!-- Shoulders (Push) -->
            <path d="M38 36 C34 36 30 40 30 46 C30 52 35 56 38 56 Z" class="${isPush ? 'active-push' : ''}" />
            <path d="M82 36 C86 36 90 40 90 46 C90 52 85 56 82 56 Z" class="${isPush ? 'active-push' : ''}" />
            
            <!-- Chest (Push) -->
            <path d="M42 36 L78 36 L74 58 L46 58 Z" class="${isPush ? 'active-push' : ''}" />
            
            <!-- Biceps (Pull) -->
            <path d="M28 56 C26 56 24 64 26 74 C28 84 32 84 32 74 Z" class="${isPull ? 'active-pull' : ''}" />
            <path d="M92 56 C94 56 96 64 94 74 C92 84 88 84 88 74 Z" class="${isPull ? 'active-pull' : ''}" />
            
            <!-- Triceps (Push) -->
            <path d="M32 56 L34 76 L38 76 L38 56 Z" class="${isPush ? 'active-push' : ''}" />
            <path d="M88 56 L86 76 L82 76 L82 56 Z" class="${isPush ? 'active-push' : ''}" />
            
            <!-- Back (Pull) -->
            <path d="M46 58 L74 58 L70 86 L50 86 Z" class="${isPull ? 'active-pull' : ''}" />
            
            <!-- Hips / Glutes -->
            <path d="M44 86 L76 86 L72 102 L48 102 Z" class="${isLegs ? 'active-legs' : ''}" />
            
            <!-- Quadriceps (Legs) -->
            <path d="M44 102 L59 102 L56 142 L46 142 Z" class="${isLegs ? 'active-legs' : ''}" />
            <path d="M61 102 L76 102 L74 142 L64 142 Z" class="${isLegs ? 'active-legs' : ''}" />
            
            <!-- Calves (Legs) -->
            <path d="M46 142 L55 142 L53 182 L48 182 Z" class="${isLegs ? 'active-legs' : ''}" />
            <path d="M65 142 L74 142 L72 182 L67 182 Z" class="${isLegs ? 'active-legs' : ''}" />
            
            <!-- Arms / Forearms -->
            <path d="M26 74 L22 105 L27 105 L32 74 Z" />
            <path d="M94 74 L98 105 L93 105 L88 74 Z" />
        </svg>
        `;
    }
}

// Global initialization
window.addEventListener('DOMContentLoaded', () => {
    window.app = new GlowFitApp();
    
    // Register Service Worker for PWA
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('sw.js')
            .then(reg => console.log('Service Worker registrado com sucesso!', reg))
            .catch(err => console.warn('Erro ao registrar Service Worker:', err));
    }
});
