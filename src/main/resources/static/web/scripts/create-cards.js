Vue.createApp({
    data() {
        return {
            data: [],
            allCards: [],
            debit: [],
            credit: [],
            clientAccounts: [],
            transactionAuxiliar: [],

            userName: "",
            userEmail: "",
            currentDate: "",
            cardTypeSelect: "",
            cardColorSelect: "",
            cardNameSelect: "",
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.data = res.data
                this.allCards = res.data.cards
                this.allCards = this.allCards.filter(card => card.disabled == false)
                res.data.cards.forEach(cards => cards.cardType == 'DEBIT' && !cards.disabled ? this.debit.push(cards) : cards.cardType == 'CREDIT' && !cards.disabled ? this.credit.push(cards) : null)

                this.debit = this.debit.filter(card => card.disabled == false)
                this.credit = this.credit.filter(card => card.disabled == false)
                //Name, email and different dates
                this.userName = res.data.firstName + " " + res.data.lastName
                this.userEmail = res.data.email
            })
    },

    methods: {
        toggleNavbar() {
            const toggle = document.querySelector("#header-toggle"),
                nav = document.querySelector("#nav-bar"),
                headerpd = document.querySelector("#header")

            setTimeout(() => {
                toggle.classList.toggle("closer")
                if (toggle.classList.contains("closer")) {
                    toggle.textContent = "close"
                } else {
                    toggle.textContent = "menu"
                }
            }, 200);

            nav.classList.toggle('show-nav')
            headerpd.classList.toggle('body-pd')
        },

        createCard(){
            axios.post('/api/clients/current/cards',
            `type=${this.cardTypeSelect}&color=${this.cardColorSelect}&cardName=${this.cardNameSelect}`,
            {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => window.location.href = "/web/cards.html")
        },

        signOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "/web/index.html")
        }
    },
    
    computed: {
        
    },

    mounted() {
        window.addEventListener('load', () => {
            let loader = document.querySelector('.loader-index')
            if (loader) {
                loader.classList.remove('loader--active')
            }
        })
    },
}).mount('#app')


